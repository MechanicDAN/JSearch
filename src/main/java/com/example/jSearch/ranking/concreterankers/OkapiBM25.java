package com.example.jSearch.ranking.concreterankers;

import com.example.jSearch.ranking.Ranker;
import com.example.jSearch.webcrawler.Document;


import java.util.List;

import static com.example.jSearch.utils.TFIDF.computeIDF;
import static com.example.jSearch.utils.TFIDF.computeTF;

public class OkapiBM25 implements Ranker {

    private static final double k1 = 1.2;
    private static final double b = 0.75;
    private List<Document> documents;

    public void setDocuments(List<Document> docs){
        this.documents = docs;
    }

    private double computeAvrdl() {
        long lenOfWholeDocs = 0;
        for (Document doc : documents) {
            lenOfWholeDocs += doc.getBody().length();
        }
        return (double) lenOfWholeDocs / documents.size();
    }

    @Override
    public double doPageRanking(String query,  Document doc) {
        double avrdl = computeAvrdl();
        double score = 0.0;
        for (String word : query.split(" ")) {
            double tf = computeTF(word, doc.getBody());
            double idf = computeIDF(word, documents);
            score += idf * (tf * (k1 + 1)) / tf + k1 * (1 - b + b * doc.getBody().length() / avrdl);
        }

        return score;
    }
}
