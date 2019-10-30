package com.example.jSearch.ranking.concreterankers;

import com.example.jSearch.ranking.AbstractRanker;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.jSearch.utils.TFIDF.computeIDF;
import static com.example.jSearch.utils.TFIDF.computeTF;

public class OkapiBM25Ranker implements AbstractRanker {

    private static final double k1 = 1.2;
    private static final double b = 0.75;
    private List<String> documents;
    private int numOfDocs;


    @Override
    public Map<Double, String> doPageRanking(String[] query, List<String> docs) {
        this.documents = docs;
        double avrdl = computeAvrdl();
        this.numOfDocs = documents.size();

        Map<Double, String> result = new HashMap<>();
        for (String doc : documents) {
            double score = 0.0;
            for (String word : query) {
                double tf = computeTF(word, doc);
                double idf = computeIDF(word, documents);
                score += idf * (tf * (k1 + 1)) / tf + k1 * (1 - b + b * doc.length() / avrdl);
                result.put(score, doc);
            }
        }

        return result;
    }


    private double computeAvrdl() {
        long lenOfWholeDocs = 0;
        for (String doc : documents) {
            lenOfWholeDocs += doc.length();
        }
        return (double) lenOfWholeDocs / numOfDocs;
    }
}
