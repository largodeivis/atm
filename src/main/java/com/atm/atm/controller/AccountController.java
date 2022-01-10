package com.atm.atm.controller;

import com.atm.atm.exception.InvalidCredentialsException;
import com.atm.atm.model.Account;
import com.atm.atm.model.AccountTransaction;
import com.atm.atm.service.AccountService;
import com.atm.atm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    private UserService userService;

    @GetMapping("users/{userId}/{pin}/account")
    public Optional<Account> retrieveAccount(@PathVariable Long userId, @PathVariable String pin) throws InvalidCredentialsException {
        return userService.retrieveAccount(userId, pin);
    }

    @PostMapping("users/{userId}/{pin}/account/deposit")
    public ResponseEntity<Void> deposit(@PathVariable Long userId, @PathVariable String pin,
                                        @RequestBody AccountTransaction transaction) throws InvalidCredentialsException {
        Optional<Account> account = accountService.depositMoney(userId, pin, transaction.getAmount());
        if (account.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("users/{userId}/{pin}/account/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable Long userId, @PathVariable String pin,
                                         @RequestBody AccountTransaction transaction) throws InvalidCredentialsException {

        Optional<Account> account = accountService.withdrawMoney(userId, pin, transaction.getAmount());
        if (account.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
