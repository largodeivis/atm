package com.atm.atm.service;

import com.atm.atm.controller.AccountController;
import com.atm.atm.exception.InsufficientBalanceException;
import com.atm.atm.jpa.AccountRepository;
import com.atm.atm.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    UserService userService;

    @Autowired
    AccountRepository accountRepository;

    public BigDecimal retrieveBalance(long userId){
        Optional<Account> account = userService.retrieveAccount(userId);
        if (account.isPresent()){
            return account.get().getBalance();
        }
        return null;
    }

    public Optional<Account> depositMoney(long userId, BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO) < 0){
            logger.error("Amount to deposit was negative. Amount: " + amount);
            return Optional.empty();
        }

        Optional<Account> account = userService.retrieveAccount(userId);
        if (account.isEmpty()){
            logger.error("Account ID doesn't exist. Account ID: " + userId);
            return Optional.empty();
        }

        BigDecimal newAmount = retrieveBalance(userId).add(amount);
        account.get().setBalance(newAmount);
        Account account1 = accountRepository.save(account.get());
        return Optional.of(account1);
    }

    public Optional<Account> withdrawMoney(long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0){
            logger.error("Amount to withdraw was negative. Amount: " + amount);
            return Optional.empty();
        }
        Optional<Account> account = userService.retrieveAccount(userId);
        if (account.isEmpty()){
            logger.error("Account ID doesn't exist. Account ID: " + userId);
            return Optional.empty();
        }
        BigDecimal currentBalance = retrieveBalance(userId);
        BigDecimal newAmount = currentBalance.subtract(amount);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0){
            logger.error("Cannot withdraw more than your current balance. Current Balance: " + currentBalance + ". Withdraw amount: " + amount);
            return Optional.empty();
        }
        Account account1 = account.get();
        account1.setBalance(newAmount);
        accountRepository.save(account1);
        return Optional.of(account1);
    }
}
