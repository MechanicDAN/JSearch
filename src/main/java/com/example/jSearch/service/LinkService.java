package com.example.jSearch.service;

import com.example.jSearch.repository.LinkRepository;
import com.example.jSearch.entity.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkService {
    @Autowired
    private LinkRepository linkRepository;

    public List<Link> getAll() {
        return linkRepository.findAll();
    }

}

