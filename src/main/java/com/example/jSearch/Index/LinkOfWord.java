package com.example.jSearch.Index;

import java.util.ArrayList;
import java.util.HashSet;

public class LinkOfWord {
    public ArrayList ReturnIndex (String InputWords){
        String[] words = InputWords.split(" ");
        HashSet pages = (HashSet) Index.getIndex().get(words[0]);
        for(String word : words){
            pages.retainAll((HashSet) Index.getIndex().get(word));
        }
        return new ArrayList<>(pages);
    }
}
