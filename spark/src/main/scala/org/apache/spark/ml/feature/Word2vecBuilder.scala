package org.apache.spark.ml.feature

/**
  *
  */
object Word2vecBuilder {

  def buildModel(map: Map[String, Array[Float]]): org.apache.spark.ml.feature.Word2VecModel = {
    val oldModel = new org.apache.spark.mllib.feature.Word2VecModel(map)
    new org.apache.spark.ml.feature.Word2VecModel("uuid_word2vec", oldModel)
  }

}
