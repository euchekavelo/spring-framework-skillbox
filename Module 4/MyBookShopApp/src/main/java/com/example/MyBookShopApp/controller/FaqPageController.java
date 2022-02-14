package com.example.MyBookShopApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FaqPageController {

    @GetMapping("/faq")
    public String faqPage() {
        return "faq";
    }

}
