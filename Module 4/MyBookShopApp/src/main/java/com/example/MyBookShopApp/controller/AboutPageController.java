package com.example.MyBookShopApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutPageController {

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

}
