package com.example.jSearch.webcrawler;

public class DirtyData {
    public String url;
    public String title;
    public String text;

    public DirtyData(String _url, String _title, String _text) {
        url = _url;
        title = _title;
        text = _text;
    }


    @Override
    public String toString() {
        return "url: " + url + "\n" +
                "title: " + title + "\n" +
                "text: " + text;
    }
}
