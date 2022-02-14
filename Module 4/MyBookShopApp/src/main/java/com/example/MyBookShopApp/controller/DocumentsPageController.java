package com.example.MyBookShopApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentsPageController {

    @GetMapping("/documents")
    public String documentsPage() {
        return "documents/index";
    }

}
