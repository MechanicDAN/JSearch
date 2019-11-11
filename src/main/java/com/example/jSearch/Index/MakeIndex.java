package com.example.jSearch.Index;

import com.example.jSearch.utils.NLPUtils;
import com.example.jSearch.webcrawler.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.*;
import java.util.*;

public class MakeIndex {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static HashMap<String, ArrayList<String>> index = new HashMap<>();
    static MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017));
    static MongoDatabase db = mongoClient.getDatabase( "Jsearch" );
    static MongoCollection indexCollection = db.getCollection("Index");
    static MongoCollection pagesCollection = db.getCollection("Pages");

    public static void Make() throws FileNotFoundException {
        db.drop();
        String path = "C:/Users/Dan/IdeaProjects/gson/pages/";
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            FileReader reader = new FileReader(file.getPath());
            try {
                Page page = GSON.fromJson(reader, Page.class);
                AddPageInMap(page);
            } catch (Exception e) {
                System.out.println(file.getPath());
            }
        }
        for(String key : index.keySet()){
            Document doc = new Document();
            IndexElement indexELToJson = new IndexElement(index.get(key));
            String json = GSON.toJson(indexELToJson);
            doc.put("key",key);
            doc.put("value",json);
            indexCollection.insertOne(doc);
        }
    }

    static void AddPageInMap(Page page) {
        List<String> normalWordsOfBody = NLPUtils.lemmatize(page.getBody()+ " " + page.getTitle());
        page.setBody(String.join(" ", normalWordsOfBody));
        ArrayList<String> addWords = new ArrayList<>();
       SavePageToMOngo(page);
        for (String word : normalWordsOfBody) {
            if (!index.containsKey(word)) {
                index.put(word, new ArrayList<String>(Collections.singletonList(page.getUrl())));
                addWords.add(word);
            } else
                if(!addWords.contains(word)) {
                    ArrayList arrayList = index.get(word);
                    arrayList.add(page.getUrl());
                    index.put(word, arrayList);
                    addWords.add(word);
                }
        }
    }

    static void SavePageToMOngo(Page page){
            Document doc = new Document();
            String json = GSON.toJson(page);
            String url = page.getUrl();
            doc.put("key",url);
            doc.put("value",json);
            pagesCollection.insertOne(doc);
        }
}

