package com.example.jSearch.webcrawler;

public class Page {
    private String title;
    private String url;
    private String body;

    public Page(String url, String title, String body) {
        this.url = url;
        this.title = title;
        this.body = body;
    }


    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
