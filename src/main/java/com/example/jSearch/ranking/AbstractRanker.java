package com.example.jSearch.ranking;

import java.util.List;

public interface AbstractRanker {
    List<String> doPageRanking(String query, List<String> docs);
}

