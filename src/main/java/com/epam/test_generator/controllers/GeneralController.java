package com.epam.test_generator.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GeneralController {

    @RequestMapping(value = "/")
    public String getMainPage() {
        return "/newSuits";
    }

    @RequestMapping(value = "/suggestion_manager")
    public String getStepSuggestionsPage() {
        return "/stepSuggestions";
    }

}