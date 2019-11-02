package com.example.jSearch.ranking;

import com.example.jSearch.webcrawler.Document;

public interface Ranker {
    double doPageRanking(String query, Document doc);
}

