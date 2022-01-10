package com.atm.atm.controller;

import com.atm.atm.exception.InvalidAmountException;
import com.atm.atm.exception.InvalidCredentialsException;
import com.atm.atm.model.Account;
import com.atm.atm.model.AccountTransaction;
import com.atm.atm.service.AccountService;
import com.atm.atm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    private UserService userService;

    @GetMapping("user/account")
    public Optional<Account> retrieveAccount(@RequestParam("userid") Long userId, @RequestParam("pin") String pin) throws InvalidCredentialsException {
        return userService.retrieveAccount(userId, pin);
    }

    @PostMapping("account/deposit")
    public ResponseEntity<String> deposit(@RequestParam("userid") Long userId, @RequestParam("pin") String pin,
                                        @RequestBody AccountTransaction transaction) throws HttpMessageNotReadableException {
        try {
            String moneyDeposited = accountService.depositMoney(userId, pin, transaction.getAmount());
            return new ResponseEntity<>(moneyDeposited, HttpStatus.OK);
        } catch (InvalidCredentialsException | InvalidAmountException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("account/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam("userid") Long userId, @RequestParam("pin") String pin,
            @RequestBody AccountTransaction transaction) throws HttpMessageNotReadableException {
        try {
            String moneyWithdrew = accountService.withdrawMoney(userId, pin, transaction.getAmount());
            return new ResponseEntity<>(moneyWithdrew, HttpStatus.OK);
        } catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
