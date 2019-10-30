package com.example.jSearch.utils;


import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;

import java.io.File;

public class NLPUtils {
    private static Word2Vec word2Vec;
    static {
        word2Vec = WordVectorSerializer.readWord2VecModel(new File("models/word2vecmodel.txt"));
    }

    public static Word2Vec getWord2Vec() {
        return word2Vec;
    }


}
