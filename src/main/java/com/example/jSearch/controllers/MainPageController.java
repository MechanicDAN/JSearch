
package com.example.jSearch.controllers;

import com.example.jSearch.forms.SearchingForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;




@Controller
public class MainPageController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("searchingform", new SearchingForm());
        return "index";
    }
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String response(SearchingForm form){
        String request = form.getSearchRequest();
        return "redirect:/search?q=" + request.replace(" ", "+");
    }

    @GetMapping(value = "/search")
    public String getResponse(@RequestParam String q, Model model){
        model.addAttribute("queryResult", null);
        return "response";
    }

}