package com.avricot.word2vec

import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}
import java.util.UUID
import javax.inject.Inject

import com.avricot.search.front.avro.SearchDetail1
import com.avricot.search.front.service.SerializationService
import com.avricot.search.front.util.DateUtils
import com.datastax.driver.core.utils.UUIDs
import org.apache.spark.{SparkConf, SparkContext}


object SplitLine {

  val serializationService = new SerializationService

  def main(args: Array[String]) {
    val (path_input, path_ouput)= args.length match {
      case 2 => (args(0), args(1))
      case _ => ("/home/quentin/Downloads/wiki.fr.mini.text", "/home/quentin/Downloads/wiki.fr.mini.text.1000")
    }
    //val sparkSession = SparkSession.builder().appName("Decode").master("local").getOrCreate()

    //import sparkSession.implicits._
    import com.datastax.spark.connector._

    val conf=new SparkConf(true)
      .set("spark.cassandra.connection.host","localhost")
      .set("spark.cassandra.connection.port", "9042")
      .set("spark.cassandra.connection.compression", "LZ4")

    val sc = new SparkContext("local","the_keyspace",conf)
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    val localDateTime = LocalDateTime.parse("2015-04-04 12:30", formatter)
    val timestamp = localDateTime.atZone(ZoneId.of("UTC")).toInstant.toEpochMilli
    val min = UUIDs.startOf(timestamp)
    val rdd = sc.cassandraTable("smartsearch", "search").select("content").where("searchTime > ?", min)

    case class SearchAggregation(clientId:String, searchType:String, searchTime:Long, precision:Int, count:Int)
    val test = rdd.map(row => {
      val buffer = row.getBytes("content")
      val bytes = new Array[Byte](buffer.remaining())
      buffer.get(bytes);
      val search = serializationService.deserializeSearchDetail(bytes)
      val timeMinute = DateUtils.roundTimestampToMinute(search.getSearchTime())

      (timeMinute, SearchAggregation(search.getClientId, search.getSearchType, timeMinute, 1, 1))
    }).reduceByKey((a,b) => SearchAggregation(a.clientId, a.searchType, a.searchTime, a.precision,  a.count+ b.count)).map(_._2)

    //test.saveToCassandra("smartsearch", "search_aggregation", SomeColumns("clientid" as "_2.clientId", "searchtype" as "_2.searchType", "precision" as "_2.precision", "searchtime" as "_1", "count" as "_2.count"))
    //test.saveToCassandra("smartsearch", "search_aggregation", SomeColumns("clientid" as "clientId", "searchtype" as "searchType", "precision" as "precision", "searchtime" as "searchTime", "count" as "count"))
      //.saveToCassandra("test", "words", SomeColumns("word" as "_2", "count" as "_1"))
//
//    CREATE TABLE IF NOT EXISTS smartsearch.search_aggregation (
//      clientId uuid,
//      searchType text,
//      precision int,
//      searchTime int,
//      count int,
//      PRIMARY KEY((clientId, searchType, precision), searchTime));


    test.collect().foreach(println)

//    val input = sparkSession.read.text(path_input).as[String]
//    input.show()
//    val grouped = input.flatMap(_.split(" ").grouped(1000).map(_.mkString(" ")))
//    grouped.show()
//    FileUtils.deleteDirectory(new File(path_ouput))
//    grouped.coalesce(1).write.csv(path_ouput)
  }
}
