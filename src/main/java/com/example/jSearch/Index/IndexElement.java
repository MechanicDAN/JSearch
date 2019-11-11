package com.example.jSearch.Index;

import com.example.jSearch.webcrawler.Page;
import java.util.ArrayList;
import java.util.HashMap;

public class IndexElement {
    private ArrayList<String> indexEl = new ArrayList<>();
    public IndexElement (ArrayList<String> indexEl){
        this.indexEl = indexEl;
    }

    public ArrayList<String> getIndexEl(){return indexEl;}
}