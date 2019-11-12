package com.example.jSearch.service;


import com.example.jSearch.entity.Activity;
import com.example.jSearch.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository activityRepo;

    @Autowired
    public ActivityService(ActivityRepository activityRepo) {
        this.activityRepo = activityRepo;
    }

    public Activity save(Activity activity) {
        return this.activityRepo.save(activity);
    }

    public Activity findFirst() {
        return this.activityRepo.findFirstBy();
    }

    public List<Activity> findAll() {
        return this.activityRepo.findAll();
    }

    public Activity findByParameter(String parameter){
        return activityRepo.findActivityByParameters(parameter);
    }

    public void delete(Long id) {
        activityRepo.deleteById(id);
    }
}
