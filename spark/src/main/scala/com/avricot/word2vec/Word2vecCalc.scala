package com.avricot.word2vec

import org.apache.hadoop.fs.Path
import org.apache.spark.ml.util.DefaultParamsReader
import org.apache.spark.sql.{Column, Row, SparkSession}
import java.util.Arrays

import org.apache.spark.ml.feature.{Word2VecModel, Word2vecBuilder}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.storage.StorageLevel

import scala.collection.mutable

object Word2vecCalc {

  def main(args: Array[String]) {
    val (path_model, master_url, words)= args.length match {
      case 2 => (args(0), "spark://spark:7077", args(1))
      case _ => ("/home/quentin/Downloads/word2vec-gensim-model", "local", "reine,rois,paris,art")
    }
    val sparkSession = SparkSession.builder().appName("Word2vec").master(master_url).config("spark.driver.maxResultSize", "30g").getOrCreate()
    import sparkSession.implicits._
    import org.apache.spark.sql.functions._

    val file = sparkSession.read.text(path_model)
    val data = file.rdd.mapPartitionsWithIndex { (idx, iter) => if (idx == 0) iter.drop(1) else iter }
    val schema = StructType(StructField("data", StringType, false) :: Nil)
    val df = sparkSession.createDataFrame(data, schema)
    val map = df.collect().map(r => {
      val strs = r.get(0).asInstanceOf[String].split(" ")
      (strs.head, strs.tail.map(_.toFloat))
    }).toMap


//    def extractWord = udf((row: String) => row.substring(0, row.indexOf(" ")))
//    def extractVectors = udf((row: String) => row.split(" ").zipWithIndex.filter(_._2>1).map(_._1.toFloat))
//    val df2 = df.withColumn("word", extractWord(df("data")))
//                .withColumn("vector", extractVectors(df("data")))
//                .drop("data")
//    df2.persist(StorageLevel.MEMORY_ONLY)
//    val nElements = df2.select("vector").first.getList(0).size
//    df2.select($"word" +: Range(0, nElements).map(idx => $"vector"(idx) as "v" + (idx)):_*).show()
//    df2.unpersist()
    //Map[String, Array[Float]]
      //val model = new feature.Word2VecModel(map)

//    val map = df2.collect().map(r => (r.get(0).asInstanceOf[String], r.get(1).asInstanceOf[mutable.WrappedArray[Float]].toArray)).toMap
    val model = Word2vecBuilder.buildModel(map)
//    val className = classOf[Word2VecModel].getName
//    val metadata = DefaultParamsReader.loadMetadata(path, sc, className)
//      DefaultParamsReader.getAndSetParams(model, metadata)
    words.split(",").foreach(w => {
      println("synonnyms of: "+w)
      model.findSynonyms(w,5).show()
    })

  }

}