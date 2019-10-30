package com.example.jSearch.ranking.concreterankers;

import com.example.jSearch.ranking.AbstractRanker;
import com.example.jSearch.utils.NLPUtils;
import com.example.jSearch.utils.TFIDF;
import org.deeplearning4j.models.word2vec.Word2Vec;
import java.util.*;
import java.util.stream.Collectors;

public class SemanticSimilarity implements AbstractRanker {
    private Word2Vec word2Vec = NLPUtils.getWord2Vec();

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
    public List<String> doPageRanking(String query, List<String> docs) {
        Map<Double, String> rankedPages = new HashMap<>();
        double[] queryVec = convertDoc2Vec(query);
        for (String doc : docs) {
            double[] docVec = convertDoc2Vec(doc);
            double cosSim = cosineSimilarity(queryVec, docVec);
            rankedPages.put(cosSim, doc);
        }
        return rankedPages.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
