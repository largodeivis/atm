package com.atm.atm.controller;

import com.atm.atm.model.Account;
import com.atm.atm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("users/{userId}/account")
    public Account retrieveAccount(@PathVariable Long userId){
        return service.retrieveAccount(userId);
    }

}
