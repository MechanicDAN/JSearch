package com.example.jSearch.ranking;


import com.example.jSearch.ranking.concreterankers.OkapiBM25;
import com.example.jSearch.ranking.concreterankers.SemanticSimilarity;

public class Rankers {

    public static OkapiBM25 newOkapiBM25Ranker() {
        return new OkapiBM25();
    }

    public static SemanticSimilarity newSemanticSimilarity() {
        return new SemanticSimilarity();
    }

}
