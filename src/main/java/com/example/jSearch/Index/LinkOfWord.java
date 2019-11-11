package com.example.jSearch.Index;

import com.example.jSearch.webcrawler.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class LinkOfWord {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static ArrayList<Page> returnPages (String inputWords){
        ArrayList<PageWithId> pageWithId = new ArrayList<>();
        ArrayList<String> input = new ArrayList<String>(Arrays.asList(inputWords.split(" ")));
        MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017));
        MongoDatabase db = mongoClient.getDatabase( "Jsearch" );
        MongoCollection collection = db.getCollection("Index");

        FindIterable<Document> firstWordDocuments = collection.find(Filters.eq("word", input.get(0)));
        for (Document found : firstWordDocuments) {
            String str = found.get("value").toString();
            Index index = GSON.fromJson(str,Index.class);
            pageWithId.addAll(index.getIndex());
        }

        if (input.size() == 1) return ArrayToPage(pageWithId);
        else{
            input.remove(0);
            for(String word : input){
                FindIterable<Document> documents = collection.find(Filters.eq("word", word));
                for (Document found : documents) {
                    String str = found.get("value").toString();
                   Index index = GSON.fromJson(str,Index.class);
                    pageWithId.removeIf(x -> myEqual(x.getId(),index.getIndex()));
               }
        }
        return ArrayToPage(pageWithId);
        }
    }
    public static boolean myEqual(int x,ArrayList<PageWithId> pages){
        for (PageWithId page : pages){
            if (x == page.getId()) return false;
    }
        return true;
    }
    public static ArrayList<Page> ArrayToPage(ArrayList<PageWithId> pageWithIds){
        ArrayList<Page> output = new ArrayList<>();
        for (PageWithId page : pageWithIds) {
            output.add(page.getPage());
        }
        return output;
    }
}