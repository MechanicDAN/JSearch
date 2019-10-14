package com.example.jSearch.webcrawler;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;


public class ThreadCrawler implements Runnable {
    private final int MAX_DEPTH = 2;
    private final String theURL;
    private final Executor executor;
    private final String parentURL;
    private Crawler crawler;
    private int level;

    public ThreadCrawler(
            int level,
            String url,
            String parentURL,
            Executor executor,
            Crawler crawler) {
        this.level = level;
        this.theURL = url;
        this.parentURL = parentURL;
        this.executor = executor;
        this.crawler = crawler;
    }

    private List<String> processPage() {
        List<String> links = new ArrayList<>();
        try {
            Connection.Response response = Jsoup.connect(theURL)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                    .timeout(4000)
                    .ignoreHttpErrors(true)
                    .execute();
            int statusCode = response.statusCode();
            if (statusCode == 200) {
                Document doc = Jsoup.connect(theURL).get();
                saveToFile(doc, theURL);
                links.addAll(doc.select("a[href]")
                        .stream()
                        .map(link -> fixURL(link.attr("href")))
                        .collect(Collectors.toList()));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());

        }
        return links;
    }

    private void saveToFile(Document doc, String url) {
        DirtyData dirtyData = new DirtyData(url, doc.title(), doc.text());
        Gson gson = new Gson();
        byte[] textBytes = gson.toJson(dirtyData).getBytes();
        try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(
                Paths.get("files\\" +
                        doc.title().replaceAll("\\W+", "") + ".json")))) {
            for (byte eachBytes : textBytes) {
                outputStream.write(eachBytes);
            }
        } catch (IOException ioe) {
            System.err.println("IO Exception: " + doc.title());
        }
    }


    private String fixURL(String rawURL) {
        if (rawURL.startsWith("#")) {
            return "";
        } else if (rawURL.startsWith("//")) {
            return rawURL.replaceFirst("//", "https://");
        } else if (rawURL.startsWith("/")) {
            try {
                String host = new URL(parentURL).getHost();
                return rawURL.replaceFirst("/", "https://" + host + "/");
            } catch (MalformedURLException e) {
                System.err.println(e.getMessage());
                return "";
            }
        } else {
            return rawURL;
        }
    }


    @Override
    public void run() {
        List<String> newUrls = processPage();
        System.out.println(theURL + " processed!");
        if (level < MAX_DEPTH) {
            for (String newUrl : newUrls) {
                if (!crawler.isVisited(newUrl)) {
                    if (newUrl.contains(parentURL)) {
                        crawler.markAsSeen(newUrl);
                        executor.execute(new ThreadCrawler(level + 1, newUrl, parentURL, executor, crawler));
                    } else {
                        crawler.addToMainQueue(newUrl);
                    }
                }
            }
        }
    }


}
