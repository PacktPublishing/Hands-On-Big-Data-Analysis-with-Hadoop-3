package com.tomekl007.sparkstreaming

import java.time.{ZoneOffset, ZonedDateTime}

import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._
import org.scalatest.Matchers._

import scala.collection.mutable
import scala.language.postfixOps
import scala.util.Random

class FilterBotsJobTest extends SparkStreamingSuite {

  private val underTest = new FilterBotsJob()

  override def appName: String = this.getClass.getSimpleName


  test("should filter all bots PageViews") {
    //given
    val pageView1 = PageView(Random.nextInt(), "userId1", "www.proper-url.com", ZonedDateTime.now(ZoneOffset.UTC))
    val pageView2 = PageView(Random.nextInt(), "userId1", "www.bot.com", ZonedDateTime.now(ZoneOffset.UTC))
    val input = Seq(pageView1, pageView2)
    val expectedOutput: Array[PageViewWithViewCounter] = Array(
      PageViewWithViewCounter.withVisitCount(pageView1, 1)
    )

    val pageViews = mutable.Queue[RDD[PageView]]()
    val streamingResults = mutable.ListBuffer.empty[Array[(PageViewWithViewCounter)]]
    val results = underTest.processPageViews(ssc.queueStream(pageViews))
    results.foreachRDD((rdd: RDD[(PageViewWithViewCounter)], time: Time) => streamingResults += rdd.collect)

    ssc.start()

    //when
    pageViews += spark.makeRDD(input)
    assertInputMatchExpected(streamingResults, expectedOutput)
  }


  test("should filter all bots PageViews and do not process duplicates (important when source works in at least once delivery)") {
    //given
    val duplicateEventTime = ZonedDateTime.now(ZoneOffset.UTC)
    val duplicatedId = Random.nextInt()
    val pageView1 = PageView(duplicatedId, "userId1", "www.proper-url.com/firstProduct", duplicateEventTime)
    val pageView1Duplicated = PageView(duplicatedId, "userId1", "www.proper-url.com/firstProduct", duplicateEventTime)
    val pageView2 = PageView(Random.nextInt(), "userId1", "www.bot.com", ZonedDateTime.now(ZoneOffset.UTC))
    val input = Seq(pageView1, pageView1Duplicated, pageView2)
    val expectedOutput: Array[PageViewWithViewCounter] = Array(
      PageViewWithViewCounter.withVisitCount(pageView1, 1)
    )

    val pageViews = mutable.Queue[RDD[PageView]]()
    val streamingResults = mutable.ListBuffer.empty[Array[(PageViewWithViewCounter)]]
    val results = underTest.processPageViews(ssc.queueStream(pageViews))
    results.foreachRDD((rdd: RDD[(PageViewWithViewCounter)], time: Time) => streamingResults += rdd.collect)

    ssc.start()

    //when
    pageViews += spark.makeRDD(input)
    assertInputMatchExpected(streamingResults, expectedOutput)
  }

  test("should count popularity of the specific url") {
    //given
    val pageView1 = PageView(Random.nextInt(), "userId1", "www.proper-url.com/popular-item", ZonedDateTime.now(ZoneOffset.UTC))
    val pageView2 = PageView(Random.nextInt(), "userId1", "www.proper-url.com/popular-item", ZonedDateTime.now(ZoneOffset.UTC))
    val input = Seq(pageView1, pageView2)
    val expectedOutput: Array[PageViewWithViewCounter] = Array(
      PageViewWithViewCounter.withVisitCount(pageView1, 1),
      PageViewWithViewCounter.withVisitCount(pageView2, 2)
    )

    val pageViews = mutable.Queue[RDD[PageView]]()
    val streamingResults = mutable.ListBuffer.empty[Array[(PageViewWithViewCounter)]]
    val results = underTest.processPageViews(ssc.queueStream(pageViews))
    results.foreachRDD((rdd: RDD[(PageViewWithViewCounter)], time: Time) => streamingResults += rdd.collect)

    ssc.start()

    //when
    pageViews += spark.makeRDD(input)
  }

}