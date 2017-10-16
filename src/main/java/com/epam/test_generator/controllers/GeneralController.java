package com.epam.test_generator.controllers;


import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GeneralController {

    @ApiOperation(value = "", hidden = true)
    @RequestMapping(value = "/")
    public String getMainPage() {
        return "/newSuits";
    }

    @ApiOperation(value = "", hidden = true)
    @RequestMapping(value = "/suggestion_manager")
    public String getStepSuggestionsPage() {
        return "/stepSuggestions";
    }

}