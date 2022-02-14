package com.example.MyBookShopApp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactsPageController {

    @GetMapping("/contacts")
    public String contactsPage() {
        return "contacts";
    }

}
