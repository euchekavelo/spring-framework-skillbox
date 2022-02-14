package com.example.MyBookShopApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GenresPageController {

    @GetMapping("/genres")
    public String genresPage() {
        return "genres/index";
    }

}
