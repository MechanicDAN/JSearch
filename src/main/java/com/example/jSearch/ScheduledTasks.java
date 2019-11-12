package com.example.jSearch;


import com.example.jSearch.entity.Activity;
import com.example.jSearch.service.ActivityService;
import com.example.jSearch.service.LinkService;
import com.example.jSearch.webcrawler.Crawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {
    private final ActivityService activityService;
    private final LinkService linkService;

    @Autowired
    public ScheduledTasks(ActivityService activityService, LinkService linkService){
        this.activityService = activityService;
        this.linkService = linkService;
    }

    //delete all activity 8, 9 and 10 o'clock of every day.
    @Scheduled(cron = "0 0 8-10 * * *")
    void deleteActivity() {
        Activity firstActivity = this.activityService.findFirst();
        List<Activity> activityList = this.activityService.findAll();
        for (Activity activity : activityList) {
            if (!activity.getId().equals(firstActivity.getId())) {
                this.activityService.delete(activity.getId());
                System.out.println("Deleted activity! " + activity.getId());
            }
        }
    }

    @Scheduled(cron= "0 0 8 * * MON-FRI")
    void crawlerTask(){
        Crawler crawler = new Crawler(linkService);
        crawler.run();
    }

}
