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
    private static HashMap<String, HashSet<PageWithId>> wordsInBody = new HashMap<>();
    private static int id = 0;

    public static void Make() throws FileNotFoundException {
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
        SaveToMOngo();
    }

    static void AddPageInMap(Page page) {
        List<String> normalWordsOfBody = NLPUtils.lemmatize(page.getBody()+ " " + page.getTitle());
        page.setBody(String.join(" ", normalWordsOfBody));
        PageWithId pageWithId = new PageWithId(id,page);
        id++;
        for (String wordsOfBody : normalWordsOfBody) {
            if (!wordsInBody.containsKey(wordsOfBody)) {
                wordsInBody.put(wordsOfBody, new HashSet<>(Collections.singletonList(pageWithId)));
            } else {
                wordsInBody.get(wordsOfBody).add(pageWithId);
            }
        }
    }

    static void SaveToMOngo(){
        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017));
        MongoDatabase db = mongoClient.getDatabase( "Jsearch" );
        MongoCollection collection = db.getCollection("Index");

        for (Map.Entry<String, HashSet<PageWithId>> element :wordsInBody.entrySet()){
            Index index = new Index(new ArrayList<PageWithId>(element.getValue()));
            Document doc = new Document();
            String json = GSON.toJson(index);
            doc.put("word",element.getKey());
            doc.put("value",json);
            collection.insertOne(doc);
        }
    }
}

