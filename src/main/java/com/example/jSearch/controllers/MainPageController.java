package com.example.jSearch.controllers;

import com.example.jSearch.forms.SearchingForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/")
public class MainPageController {



    @GetMapping
    public String index(Model model) {
        model.addAttribute("searchingform", new SearchingForm());

        return "index";
    }
}