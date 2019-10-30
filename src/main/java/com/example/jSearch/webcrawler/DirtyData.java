package com.example.jSearch.webcrawler;

public class DirtyData {
    public String url;
    public String title;
    public String[] words;

    public DirtyData(String _url, String _title, String[] _words) {
        url = _url;
        title = _title;
        words = _words;
    }


    @Override
    public String toString() {
        return "url: " + url + "\n" +
                "title: " + title + "\n" +
                "text: " + words;
    }
}
