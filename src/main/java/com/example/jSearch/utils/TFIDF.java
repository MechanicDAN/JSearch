package com.example.jSearch.utils;

import com.example.jSearch.webcrawler.Document;

import java.util.Arrays;
import java.util.List;

public class TFIDF {


    public static double computeTF(String word, String doc) {
        return Math.log(1 + Arrays.stream(doc.split("\\W+"))
                .filter(el -> el.equals(word))
                .count());
    }

    public static double computeIDF(String word, List<Document> docs) {
        int counter = 0;
        for (Document doc : docs) {
            if (doc.getBody().contains(word)) counter++;
        }
        int numOfDocs = docs.size();
        return Math.log((numOfDocs - counter + 0.5) / (counter + 0.5));
    }


}
