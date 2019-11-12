package com.example.jSearch.ranking;

import com.example.jSearch.webcrawler.Page;

public interface Ranker {
    double doPageRanking(String query, Page doc);
}

