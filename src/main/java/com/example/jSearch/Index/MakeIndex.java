package com.example.jSearch.Index;

import com.example.jSearch.utils.NLPUtils;
import com.example.jSearch.webcrawler.Document;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import java.io.*;
import java.sql.Array;
import java.util.*;

public class MakeIndex {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static HashMap<String, HashSet<Document>> wordsInBody = new HashMap<>();
    private static HashMap<String, HashSet<Document>> wordsInHead = new HashMap<>();

    public static void main(String[] args) throws FileNotFoundException {
        String path = "C:/Users/Dan/IdeaProjects/gson/pages/";
        String pathToJsonOfBody = "C:\\Users\\Dan\\IdeaProjects\\gson\\IndexOfBody.json";
        String pathToJsonOfHead = "C:\\Users\\Dan\\IdeaProjects\\gson\\IndexOfHead.json";
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            FileReader reader = new FileReader(file.getPath());
            JsonParser parser = new JsonParser();
            //пытаемся открыть json файл
            try {
                JsonObject obj = (JsonObject) parser.parse(reader);
                Document page = GSON.fromJson(obj, Document.class);
                AddPageInMap(page);
            } catch (Exception e) {
                System.out.println(file.getPath());
            }
        }
        //преводим map в json
        JSONObject wordsInBodyInGson = new JSONObject(wordsInBody);
        JSONObject wordsInHeadInGson = new JSONObject(wordsInHead);
        //пытаемся записать json в файл
        try {
            FileWriter writerInBody = new FileWriter(pathToJsonOfBody);
            wordsInBodyInGson.write(writerInBody);
            writerInBody.flush();
            writerInBody.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // добавление слов и списков ссылок где они содержаться в мапу
    static void AddPageInMap(Document page) {
        List<String> normalWordsOfBody = NLPUtils.lemmatize(page.getBody()+ " " + page.getTitle());
        page.setBody(String.join(" ", normalWordsOfBody));
        for (String wordsOfBody : normalWordsOfBody) {
            if (!wordsInBody.containsKey(wordsOfBody)) {
                wordsInBody.put(wordsOfBody, new HashSet<>(Collections.singletonList(page)));
            } else {
                wordsInBody.get(wordsOfBody).add(page);
            }
        }
    }
}
