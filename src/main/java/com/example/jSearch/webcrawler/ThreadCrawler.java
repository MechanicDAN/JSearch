
package com.example.jSearch.webcrawler;

import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;

import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;


public class ThreadCrawler implements Callable<Void> {
    private final int MAX_DEPTH = 3;
    private final String theURL;
    private final ExecutorService executor;
    private final String parentURL;
    private Crawler crawler;
    private int level;
    private Gson gson = new Gson();


    public ThreadCrawler(
            int level,
            String url,
            String parentURL,
            ExecutorService executor,
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
                org.jsoup.nodes.Document doc = Jsoup.connect(theURL).get();
                saveToFile(doc);
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

    private void saveToFile(org.jsoup.nodes.Document doc) {
        byte[] textBytes = gson.toJson(new Page(theURL, doc.title(), doc.body().text())).getBytes();
        try (OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(
                Paths.get("pages\\" +
                        doc.title().replaceAll("\\W+", "") + ".json")))) {
            for (byte eachBytes : textBytes) {
                outputStream.write(eachBytes);
            }
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
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
    public Void call() {
        if (level < MAX_DEPTH) {
            System.out.println("Thread[ " + Thread.currentThread().getName() + " ] process URL[" + theURL + "]");
            List<String> newUrls = processPage();
            List<ThreadCrawler> tasks = new ArrayList<>();
            for (String newUrl : newUrls) {
                if (!crawler.isVisited(newUrl)) {
                    if (newUrl.contains(parentURL)) {
                        crawler.markAsSeen(newUrl);
                        tasks.add(new ThreadCrawler(level + 1, newUrl, parentURL, executor, crawler));
                    }
                }
                if (Thread.currentThread().isInterrupted()) return null;
            }
            try {
                executor.invokeAll(tasks);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                crawler.shutdownAndAwaitTermination();
            }
        }
        return null;
    }
}

