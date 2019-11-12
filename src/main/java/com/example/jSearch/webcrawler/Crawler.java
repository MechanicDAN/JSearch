package com.example.jSearch.webcrawler;


import com.example.jSearch.entity.Link;
import com.example.jSearch.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;



public class Crawler implements CommandLineRunner {
    private Set<String> seenURLs = ConcurrentHashMap.newKeySet();
    private Set<String> urls = ConcurrentHashMap.newKeySet();
    private ExecutorService threadPool = Executors.newWorkStealingPool(18);
    private LinkService linkService;

    @Autowired
    public Crawler(LinkService linkService){
        this.linkService = linkService;
    }

    private void initQueue() {
        try {
            urls.addAll(linkService.getAll().stream()
                    .map(Link::getURL)
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            System.err.println("Cannot init queue");
        }
    }


    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        try {
            initQueue();
            List<ThreadCrawler> crawlerTasks = new ArrayList<>();
            for(String url : urls){
                crawlerTasks.add(new ThreadCrawler(0, url, url, threadPool, this));
            }
            threadPool.invokeAll(crawlerTasks);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            shutdownAndAwaitTermination();
        }
    }


     boolean isVisited(String URL) {
        return seenURLs.contains(URL);
    }

     void markAsSeen(String url) {
        seenURLs.add(url);
    }

    void addToMainQueue(String url) {
        urls.add(url);
    }


    public void shutdownAndAwaitTermination() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(120, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
                if (!threadPool.awaitTermination(120, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

