package com.example.jSearch.ranking.nlpmodels;


import org.deeplearning4j.models.embeddings.learning.impl.elements.CBOW;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.FileSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;
import java.io.IOException;


@SuppressWarnings("deprecation")
public class WordVecModel {

    public static void train(File trainData) {
        SentenceIterator sentenceIterator = new FileSentenceIterator(trainData);
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(2)
                .layerSize(100)
                .windowSize(5)
                .seed(42)
                .epochs(2)
                .elementsLearningAlgorithm(new CBOW<>())
                .iterate(sentenceIterator)
                .tokenizerFactory(tokenizerFactory)
                .build();

        vec.fit();

        try {
            WordVectorSerializer.writeWordVectors(vec, "models/word2vec.txt");
        } catch (IOException ioe) {
            System.err.println("Cannot save model");
        }
    }


    public static Word2Vec loadModel(){
        return WordVectorSerializer.readWord2VecModel(new File("models/word2vec.txt"));
    }

}