package com.example.jSearch.Index;
import com.example.jSearch.webcrawler.Document;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.xalan.xsltc.dom.SimpleResultTreeImpl;
import org.json.JSONObject;

import javax.json.Json;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class test {
    public static void main(String[] args) throws FileNotFoundException {
        String word = "1";
        File file = new File("C:\\Users\\Dan\\IdeaProjects\\gson\\IndexOfBody.json");
        FileReader reader = new FileReader(file.getPath().toString());
        JsonParser parser= new JsonParser();
        JsonObject obj = (JsonObject) parser.parse(reader);
        ArrayList<String> links = new ArrayList<>();
        for(String link : obj.get(word).toString().split(" "))
            links.add(link);
        System.out.println(links);
    }
}
