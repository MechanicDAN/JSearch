package com.example.jSearch.Index;

import com.example.jSearch.webcrawler.Page;
import java.util.ArrayList;

public class Index {
    private ArrayList<PageWithId> index;

    public Index(ArrayList<PageWithId> index){
        this.index = index;
    }

    public ArrayList<PageWithId> getIndex(){return index;}
}