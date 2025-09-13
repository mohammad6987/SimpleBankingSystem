package com.example.account_service.controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class AccountController {


    @GetMapping("/hello")
    public String hello() {
        return "Hello World from Account Service";
    }

}
