package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.etities.User;
import ru.kata.spring.boot_security.demo.services.CustomUserDetailService;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
public class MainController {
    private CustomUserDetailService customUserDetailService;
    @Autowired
    public void setUserService(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

//    @GetMapping("/")
//    public String homePage() {
//
//        return "home";
//    }
    @GetMapping("/authenticated")
    public String pageForAuthenticatedUsers(Principal principal) {
        User user = customUserDetailService.findByUsername(principal.getName());
        String userRoles = user.getRoles().stream().map(myEntity -> myEntity.getName()).collect(Collectors.joining(", "));
        return "secured part of web service: " + user.getUsername() + " " + user.getEmail() + " " + userRoles;
    }
}