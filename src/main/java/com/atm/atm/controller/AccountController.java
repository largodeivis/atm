package com.atm.atm.controller;

import com.atm.atm.exception.InsufficientBalanceException;
import com.atm.atm.exception.InvalidAmountException;
import com.atm.atm.exception.InvalidCredentialsException;
import com.atm.atm.model.AccountTransaction;
import com.atm.atm.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

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
            BigDecimal newBalance = accountService.depositMoney(userId, pin, transaction.getAmount());
            return new ResponseEntity<>(responseString(userId, newBalance), HttpStatus.OK);
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
            BigDecimal newBalance = accountService.withdrawMoney(userId, pin, transaction.getAmount());
            return new ResponseEntity<>(responseString(userId, newBalance), HttpStatus.OK);
        } catch (InvalidCredentialsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (InvalidAmountException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InsufficientBalanceException ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.OK);
        }
    }

    private String responseString(Long userId, BigDecimal balance){
        return "UserId: " + userId +"\nNew Balance: $" + balance;
    }
}
