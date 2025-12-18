package com.example.backend.controller;

import com.example.backend.common.annotations.HandleControllerGlobalException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@HandleControllerGlobalException
@RestController
public class HelloController {

    @RequestMapping("/")
    public String root() {
        return "Hello world";
    }

    @GetMapping("/hello")
    public String hello() {
        return "[GET] Hello world";
    }

    @PostMapping("/world")
    public String post() {
        return "[POST] Hello world";
    }
}
