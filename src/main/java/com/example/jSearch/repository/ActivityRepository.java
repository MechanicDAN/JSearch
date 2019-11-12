package com.example.jSearch.repository;


import com.example.jSearch.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findFirstBy();
    Activity findActivityByParameters(String parameters);
}
