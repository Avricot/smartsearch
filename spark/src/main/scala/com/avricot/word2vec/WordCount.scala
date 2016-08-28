package com.avricot.word2vec

import java.io.File
import org.apache.commons.io.FileUtils
import org.apache.spark.sql.SparkSession


object WordCount {

  def main(args: Array[String]) {
    val (path_input, path_ouput)= args.length match {
      case 2 => (args(0), args(1))
      case _ => ("/home/quentin/Downloads/wiki.fr.mini.text", "/home/quentin/Downloads/wiki.fr.mini.text.wc")
    }
    val sparkSession = SparkSession.builder().appName("Word2vec").master("local").getOrCreate()
    import sparkSession.implicits._

    val input = sparkSession.read.text(path_input).as[String]
    val wc = input.flatMap(_.split(" ")).groupBy($"value").count().sort($"count".desc)
    FileUtils.deleteDirectory(new File(path_ouput))
    wc.coalesce(1).write.csv(path_ouput)
  }
}