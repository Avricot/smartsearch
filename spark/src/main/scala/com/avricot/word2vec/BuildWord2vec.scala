package com.avricot.word2vec

import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.spark.ml.feature.{Word2Vec, Word2VecModel}
import org.apache.spark.sql.SparkSession


object BuildWord2vec {

  def main(args: Array[String]) {
    val (path_input, path_ouput, master_url)= args.length match {
      case 2 => (args(0), args(1), "spark://spark:7077")
      case _ => ("/home/quentin/Downloads/wiki.fr.mini.text", "/home/quentin/Downloads/wiki.fr.mini.text.vect", "local")
    }
    val sparkSession = SparkSession.builder().appName("Word2vec").master(master_url).getOrCreate()
    import sparkSession.implicits._

    // Input data: Each row is a bag of words from a sentence or document.
//    val word2Vec = new Word2Vec()
//      .setInputCol("value")
//      .setOutputCol("result")
//      .setMaxSentenceLength(60000)
//      .setVectorSize(500)
//      .setMinCount(5)
//      .setNumPartitions(7)
//    val input = sparkSession.read.text(path_input).as[String]
//    val model = word2Vec.fit(input.map(_.split(" ")))
    val test = Word2VecModel.load("/home/quentin/Downloads/word2vec-gensim-model")
    test.findSynonyms("art", 3).show()
//    FileUtils.deleteDirectory(new File(path_ouput))
//    model.save(path_ouput)
//    val words = input.flatMap(_.split(" ")).map(t => Seq(t)).distinct()
//    val result = model.transform(words)
//    result.take(10).foreach(println)
//    FileUtils.deleteDirectory(new File(path_ouput))
//    result.coalesce(1).write.csv(path_ouput)

//    val newModel = Word2VecModel.load(path_ouput)
//    newModel.findSynonyms("art", 3).show()
//    newModel.findSynonyms("monte", 3).show()
    //val test = sparkSession.read.text(path_ouput).as[String]

  }
}