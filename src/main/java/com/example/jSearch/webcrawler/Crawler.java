package com.example.jSearch.webcrawler;


import com.example.jSearch.entity.Link;
import com.example.jSearch.service.LinkService;
import com.example.jSearch.webcrawler.ThreadCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class Crawler implements CommandLineRunner {
    boolean stopFlag = false;
    private Set<String> seenURLs = new HashSet<>();
    private BlockingQueue<String> mainQueue = new PriorityBlockingQueue<>();
    private ExecutorService threadPool = Executors.newWorkStealingPool(18);

    @Autowired
    private LinkService linkService;

    private void initQueue() {
        try {
            mainQueue.addAll(linkService.getAll().stream()
                    .map(Link::getURL)
                    .collect(Collectors.toList()));
        } catch (NullPointerException e) {
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
            while (!stopFlag) {
                String url = mainQueue.take();
                seenURLs.add(url);
                threadPool.submit(new ThreadCrawler(0, url, url, threadPool, this));
            }
            shutdownAndAwaitTermination(threadPool);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            shutdownAndAwaitTermination(threadPool);
        }
    }


    synchronized boolean isVisited(String URL) {
        return seenURLs.contains(URL);
    }

    synchronized void markAsSeen(String url) {
        seenURLs.add(url);
    }

    synchronized void addToMainQueue(String url) {
        mainQueue.add(url);
    }


    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

