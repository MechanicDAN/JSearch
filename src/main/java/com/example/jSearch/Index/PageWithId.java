package com.example.jSearch.Index;

import com.example.jSearch.webcrawler.Page;

import java.util.ArrayList;

public class PageWithId {
    private int id;
    private Page page;

    public PageWithId(int id,Page page){
        this.id = id;
        this.page = page;
    }

    public Page getPage() {
        return page;
    }

    public int getId() {
        return id;
    }
}
