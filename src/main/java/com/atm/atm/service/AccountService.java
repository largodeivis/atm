package com.atm.atm.service;

import com.atm.atm.exception.InsufficientBalanceException;
import com.atm.atm.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


//
//import com.atm.atm.model.Account;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//
@Service
public class AccountService {

    @Autowired
    UserService userService;

    public BigDecimal retrieveBalance(long userId){
        return userService.retrieveAccount(userId).getBalance();
    }

    public BigDecimal depositMoney(long userId, BigDecimal amount){
        Account account = userService.retrieveAccount(userId);
        BigDecimal newAmount = retrieveBalance(userId).add(amount);
        account.setBalance(newAmount);
        return newAmount;
    }

    public BigDecimal withdrawMoney(long userId, BigDecimal amount) throws InsufficientBalanceException {
        Account account = userService.retrieveAccount(userId);
        BigDecimal currentBalance = retrieveBalance(userId);
        BigDecimal newAmount = currentBalance.subtract(amount);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new InsufficientBalanceException(currentBalance, newAmount);
        }
        account.setBalance(newAmount);
        return newAmount;
    }
}
