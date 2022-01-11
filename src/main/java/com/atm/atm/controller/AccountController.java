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

import java.math.BigDecimal;
import java.util.Optional;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    private UserService userService;

    @GetMapping("user/account")
    public ResponseEntity<String> retrieveBalance(@RequestParam("userid") Long userId, @RequestParam("pin") String pin){
        try {
            BigDecimal balance = accountService.retrieveBalance(userId, pin);
            return new ResponseEntity<>("User ID: " + userId.toString() + "\nBalance: $" + balance.toString(),
                    HttpStatus.OK);
        } catch (InvalidCredentialsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("account/deposit")
    public ResponseEntity<String> deposit(@RequestParam("userid") Long userId, @RequestParam("pin") String pin,
                                        @RequestBody AccountTransaction transaction) throws HttpMessageNotReadableException {
        try {
            String moneyDeposited = accountService.depositMoney(userId, pin, transaction.getAmount());
            return new ResponseEntity<>(moneyDeposited, HttpStatus.OK);
        } catch (InvalidCredentialsException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch( InvalidAmountException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("account/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam("userid") Long userId, @RequestParam("pin") String pin,
            @RequestBody AccountTransaction transaction) throws HttpMessageNotReadableException {
        try {
            String moneyWithdrew = accountService.withdrawMoney(userId, pin, transaction.getAmount());
            return new ResponseEntity<>(moneyWithdrew, HttpStatus.OK);
        } catch (InvalidCredentialsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
