package com.avricot.word2vec

import org.apache.spark.ml.feature.Word2VecModel
import org.apache.spark.sql.{Column, SparkSession}
import org.apache.spark.sql.functions._


object SparkExample {

  def main(args: Array[String]) {
    val (path_input, path_ouput, master_url)= args.length match {
      case 2 => (args(0), args(1), "spark://spark:7077")
      case _ => ("/home/quentin/Downloads/wiki.fr.mini.text", "/home/quentin/Downloads/wiki.fr.mini.text.vect", "local")
    }
    val sparkSession = SparkSession.builder().appName("Word2vec").master(master_url).getOrCreate()


    val dftest = sparkSession.createDataFrame(Seq((1L, "z", "e", "r"))).toDF("user", "address1", "address2", "address3")

    val asMap = udf((keys: Seq[String], values: Seq[String]) =>
      keys.zip(values).filter{
        case (k, null) => false
        case _ => true
      }.toMap)

    val mapData = List("address1", "address2", "address3")
    val keys: Column = array(mapData.map(lit): _*)
    val values: Column = array(mapData.map(col): _*)
    println(keys)
    println(values)
    val dfWithMap = dftest.withColumn("address", asMap(keys, values)).show()


  }
}