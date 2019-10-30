<<<<<<< HEAD
package com.example.jSearch.ranking;

import java.util.List;


public interface AbstractRanker {
    List<String> doPageRanking(String query, List<String> docs);
}
=======
package com.example.jSearch.ranking;

import java.util.List;
import java.util.Map;

public interface AbstractRanker {
    Map<Double, String> doPageRanking(String[] query, List<String> docs);
}
>>>>>>> cbd800ef2f8029caa07b200c90a226365dfa3abc
