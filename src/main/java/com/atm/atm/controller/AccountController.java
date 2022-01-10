package com.atm.atm.controller;

import com.atm.atm.model.Account;
import com.atm.atm.model.AccountTransaction;
import com.atm.atm.model.User;
import com.atm.atm.service.AccountService;
import com.atm.atm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    AccountService accountService;

    @Autowired
    private UserService userService;

    @GetMapping("users/{userId}/account")
    public Optional<Account> retrieveAccount(@PathVariable Long userId) {
        return userService.retrieveAccount(userId);
    }

    @PostMapping("users/{userId}/account/deposit")
    public ResponseEntity<Void> deposit(@PathVariable Long userId, @RequestBody AccountTransaction transaction) {
        Optional<Account> account = accountService.depositMoney(userId, transaction.getAmount());
        if (account.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("users/{userId}/account/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable Long userId, @RequestBody AccountTransaction transaction) {
        Optional<Account> account = accountService.withdrawMoney(userId, transaction.getAmount());
        if (account.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
