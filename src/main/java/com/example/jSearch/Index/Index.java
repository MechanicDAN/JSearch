package com.example.jSearch.Index;

import com.google.gson.JsonParser;

import javax.json.JsonObject;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Index {
    private static JsonObject Index;

    public Index(String path) throws FileNotFoundException {
        FileReader reader = new FileReader(path);
        JsonParser parser = new JsonParser();
        Index = (JsonObject) parser.parse(reader);
    }
    public static JsonObject getIndex(){return Index;}
}