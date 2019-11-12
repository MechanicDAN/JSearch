package com.example.jSearch.ranking.concreterankers;

import com.example.jSearch.ranking.Ranker;
import com.example.jSearch.ranking.nlpmodels.WordVecModel;
import com.example.jSearch.utils.TFIDF;
import com.example.jSearch.webcrawler.Page;
import org.deeplearning4j.models.word2vec.Word2Vec;


public class SemanticSimilarity implements Ranker {
    private final Word2Vec word2Vec = WordVecModel.loadModel();

    private double[] convertDoc2Vec(String doc) {
        double[] resultVec = new double[200];
        int numOfWords = 0;
        if (!doc.isEmpty()) {
            for (String word : doc.split(" ")) {
                if (word2Vec.hasWord(word)) {
                    numOfWords++;
                    double tf = TFIDF.computeTF(word, doc);
                    double[] wordVec = word2Vec.getWordVector(word);
                    for (int i = 0; i < resultVec.length; i++) {
                        resultVec[i] = resultVec[i] + tf * wordVec[i];
                    }
                }
            }
            if (numOfWords > 1) {
                for (int j = 0; j < resultVec.length; j++) {
                    resultVec[j] = resultVec[j] / numOfWords;
                }
                return resultVec;
            } else {
                return resultVec;
            }
        } else {
            return null;
        }
    }

    private double cosineSimilarity(double[] vec1, double[] vec2) {
        double vec1Len = 0.0;
        double vec2Len = 0.0;
        double scalarProduct = 0.0;
        for (int i = 0; i < vec1.length; i++) {
            scalarProduct += vec1[i] * vec2[i];
            vec1Len += vec1[i] * vec1[i];
            vec2Len += vec2[i] * vec2[i];
        }
        return scalarProduct / (Math.sqrt(vec1Len) + Math.sqrt(vec2Len));
    }

    @Override
    public double doPageRanking(String query, Page doc) {
        double[] queryVec = convertDoc2Vec(query);
        double[] docVec = convertDoc2Vec(doc.getBody());
        return cosineSimilarity(queryVec, docVec);
    }
}
