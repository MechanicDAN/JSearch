package com.example.jSearch.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash("results")
public class SearchResults {
    @Id
    private String id;
    private String links;


    public SearchResults(String id, String links){
        this.id = id;
        this.links = links;
    }
    public String getId() {
        return id;
    }


    public String getLinks() {
        return links;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLinks(String links) {
        this.links = links;
    }


    @Override
    public String toString() {
        return "SearchResult = {" +
                "id = " + id +" " +
                "links = " + links + " }";
    }
}
