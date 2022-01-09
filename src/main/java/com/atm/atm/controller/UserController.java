package com.atm.atm.controller;

import com.atm.atm.model.Account;
import com.atm.atm.model.User;
import com.atm.atm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("users/{userId}/account")
    public Account retrieveAccount(@PathVariable Long userId){
        return service.retrieveAccount(userId);
    }

    @GetMapping("users/{userId}")
    public User retrieveUser(@PathVariable Long userId){
        return service.retrieveUser(userId);
    }

    @GetMapping("users/")
    public List<User> retrieveUsers(){
        return service.retrieveAllUsers();
    }

    @PostMapping("users/create-user")
    public ResponseEntity<Void> createUser(@RequestBody User newUser){
        User user = service.createUser(newUser);
        if(user == null){
            return ResponseEntity.noContent().build();
        }

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

}
