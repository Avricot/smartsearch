package com.avricot.word2vec

import java.io._

import org.deeplearning4j.models.word2vec.Word2Vec
import org.deeplearning4j.plot.BarnesHutTsne
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import java.util.ArrayList

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;

/**
  *
  */
object WordViz {
  def main(args: Array[String]) {
    val (path_input, path_ouput, master_url)= args.length match {
      case 2 => (args(0), args(1), "spark://spark:7077")
      case _ => ("/home/quentin/Downloads/wiki.fr.mini.text", "/home/quentin/Downloads/wiki.fr.mini.text.vect", "local")
    }

    val iter = new LineSentenceIterator(new File(path_input));
    // Split on white spaces in the line to get words
    val t = new DefaultTokenizerFactory();
    //lower & punctuation already done
    //t.setTokenPreProcessor(new CommonPreprocessor());
    val vectorLength = 100
    val vec = new Word2Vec.Builder()
      .minWordFrequency(5)
      .iterations(1)
      .layerSize(vectorLength)
      .seed(42)
      .windowSize(5)
      .iterate(iter)
      .tokenizerFactory(t)
      .build();
    vec.fit();
    WordVectorSerializer.writeFullModel(vec, "/home/quentin/Downloads/word2vec_deep4j_model");
    println(vec.wordsNearest("art", 3))
    val tsne: BarnesHutTsne = new BarnesHutTsne.Builder()
      .setMaxIter(1000)
      .stopLyingIteration(250)
      .learningRate(500)
      .useAdaGrad(false)
      .theta(0.5)
      .setMomentum(0.5)
      .normalize(true)
      .usePca(false)
      .build();
    vec.lookupTable().plotVocab(tsne, 10, new File("/home/quentin/Downloads/test_ouput_word2vec"));

    val write: BufferedWriter = new BufferedWriter(new FileWriter(new File(""), true))

    val tsne2: BarnesHutTsne = new BarnesHutTsne.Builder()
      .setMaxIter(1000)
      .stopLyingIteration(250)
      .learningRate(500)
      .useAdaGrad(false)
      .theta(0.5)
      .setMomentum(0.5)
      .normalize(true)
      .usePca(false)
      .build();
    val array: INDArray = Nd4j.create(vec.vocab().numWords(), vectorLength)
    val plot = new ArrayList[String]()

    for (i <- 0 to vec.vocab().numWords()-1){
      val word: String = vec.vocab().wordAtIndex(i)
      plot.add(word);
      array.putRow(i, vec.getWordVectorMatrixNormalized(word))
    }
    val res = tsne2.calculate(array,2,tsne2.getPerplexity)
    println(res)
  }

}
