package com.example.jSearch.ranking;

import java.util.List;
import java.util.Map;

public interface AbstractRanker {
    Map<Double, String> doPageRanking(String[] query, List<String> docs);
}
