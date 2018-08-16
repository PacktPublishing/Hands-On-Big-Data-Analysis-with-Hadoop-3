//package com.tomekl007.mba
//
//import org.apache.spark.sql.SparkSession
//import org.scalatest.FunSuite
//import org.scalatest.Matchers._
//
//class FindAssociationRulesTest extends FunSuite {
//
//  private val spark = SparkSession.builder().master("local[2]").getOrCreate().sparkContext
//
//  test("should find counts of occurring sequences") {
//    //given
//    val transactions = spark.makeRDD(
//      List(
//        "crackers,bread,banana",
//        "crackers,coke,butter,coffee",
//        "crackers,bread",
//        "crackers,bread",
//        "crackers,bread,coffee",
//        "butter,coke",
//        "butter,coke,bread,crackers")
//    )
//
//    //when
//    val res = FindAssociationRules.findCountsOfOccurringSequences(transactions)
//      .sortBy(_._2, ascending = false)
//      .take(8)
//      .toList
//
//
//    //then
//    res should contain theSameElementsAs List(
//      (List("crackers"), 6),
//      (List("bread"), 5),
//      (List("crackers", "bread"), 4),
//      (List("coke"), 3),
//      (List("butter"), 3),
//      (List("butter", "coke"), 2),
//      (List("coffee"), 2),
//      (List("crackers", "coffee"), 2),
//    )
//
//  }
//
//  test("should find association rules") {
//    //given
//    val transactions = spark.makeRDD(
//      List(
//        "crackers,bread,banana",
//        "crackers,coke,butter,coffee",
//        "crackers,bread",
//        "crackers,bread",
//        "crackers,bread,coffee",
//        "butter,coke",
//        "butter,coke,bread,crackers"
//      )
//    )
//
//    //when
//    val res = FindAssociationRules.performMBAAnalysis(transactions)
//      .collect()
//
//    //then
//    assert(res.contains((List("crackers", "banana"), List("bread"), 1.0)))
//    assert(res.contains((List("coke"), List("bread"), 0.3333333333333333)))
//    assert(res.contains((List("bread"), List("crackers"), 0.2)))
//    assert(res.contains((List("coffee"), List("coke"), 0.5)))
//
//
//  }
//
//}
