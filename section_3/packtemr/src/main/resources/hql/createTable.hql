CREATE EXTERNAL TABLE IF NOT EXISTS analytics.page_view(
  url string,
  user_id int,
  isBot boolean,
)
PARTITIONED BY (
  update_date string )
ROW FORMAT SERDE
  'org.apache.hadoop.hive.serde2.avro.AvroSerDe'
STORED AS INPUTFORMAT
  'org.apache.hadoop.hive.ql.io.avro.AvroContainerInputFormat'
OUTPUTFORMAT
  'org.apache.hadoop.hive.ql.io.avro.AvroContainerOutputFormat'
LOCATION
  'hdfs://table/page_view'
  TBLPROPERTIES (
    'avro.schema.literal'='{
    "type":"record",
    "name":"PageView",
    "namespace":"com.tomekl007",
    "fields":
    [
    {"name":"url","type":{"type":"string","avro.java.string":"String"}},
    {"name":"isBot","type":"boolean"},
    {"name":"user_id","type": "int"}
    ]}')