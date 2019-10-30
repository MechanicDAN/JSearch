<<<<<<< HEAD
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
=======
package com.example.jSearch.ranking;




import com.example.jSearch.ranking.concreterankers.OkapiBM25Ranker;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Rankers {

    public static OkapiBM25Ranker newOkapiBM25Ranker(){
        return new OkapiBM25Ranker();
    }



}
>>>>>>> cbd800ef2f8029caa07b200c90a226365dfa3abc
