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
