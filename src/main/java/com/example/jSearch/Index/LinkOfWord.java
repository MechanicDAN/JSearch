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
import java.util.HashMap;
import java.util.HashSet;

public class LinkOfWord {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static MongoClient mongoClient = new MongoClient(new ServerAddress("localhost", 27017));
    static MongoDatabase db = mongoClient.getDatabase("Jsearch");
    static MongoCollection indexCollection = db.getCollection("Index");
    static MongoCollection pagesCollection = db.getCollection("Pages");

    public static ArrayList<Page> returnPages(String inputWords) {
        ArrayList<String> input = new ArrayList<String>(Arrays.asList(inputWords.split(" ")));
        ArrayList<String> linkOfPage = new ArrayList<>();
        FindIterable<Document> firstIndexEl = indexCollection.find(Filters.eq("key", input.get(0)));
        for (Document found : firstIndexEl) {
            String str = found.get("value").toString();
            linkOfPage = GSON.fromJson(str, IndexElement.class).getIndexEl();
        }
        if (input.size() == 1)
            if (linkOfPage.size() == 0)
                return new ArrayList<Page>();
            else return MakeArrayOfPages(linkOfPage);
        else {
            input.remove(0);
            for (String word : input) {
                FindIterable<Document> otherIndexEl = indexCollection.find(Filters.eq("key", word));
                for (Document found : otherIndexEl) {
                    String str = found.get("value").toString();
                    ArrayList arrayList = GSON.fromJson(str, IndexElement.class).getIndexEl();
                    linkOfPage.retainAll(arrayList);
                }
            }
            if (linkOfPage.size() == 0)
                return new ArrayList<Page>();
            else return MakeArrayOfPages(linkOfPage);
        }
    }

     static ArrayList<Page >MakeArrayOfPages(ArrayList < String > links) {
        ArrayList<Page> output = new ArrayList<>();
        System.out.println("make");
        for(String url : links){
            FindIterable<Document> pagesDocuments = pagesCollection.find(Filters.eq("key", url));
            for (Document found : pagesDocuments){
                String str = found.get("value").toString();
                Page page = GSON.fromJson(str, Page.class);
                output.add(page);
            }
        }
        return output;
    }

}