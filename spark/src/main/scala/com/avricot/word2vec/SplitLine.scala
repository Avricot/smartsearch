package com.avricot.word2vec

import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.spark.ml.feature.Word2Vec
import org.apache.spark.sql.SparkSession


object SplitLine {

  def main(args: Array[String]) {
    val (path_input, path_ouput)= args.length match {
      case 2 => (args(0), args(1))
      case _ => ("/home/quentin/Downloads/wiki.fr.mini.text", "/home/quentin/Downloads/wiki.fr.mini.text.1000")
    }

    //val sparkSession = SparkSession.builder().appName("Decode").master("local").getOrCreate()

    import sparkSession.implicits._
    import com.datastax.spark.connector._

    val conf=new SparkConf(true)
      .set("spark.cassandra.connection.host","127.0.0.1")

    val sc=new SparkContext("local","the_keyspace",conf)

    val input = sparkSession.read.text(path_input).as[String]
    input.show()
    val grouped = input.flatMap(_.split(" ").grouped(1000).map(_.mkString(" ")))
    grouped.show()
    FileUtils.deleteDirectory(new File(path_ouput))
    grouped.coalesce(1).write.csv(path_ouput)
  }
}
