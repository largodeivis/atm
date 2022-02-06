package com.atm.atm.service;

import com.atm.atm.controller.AccountController;
import com.atm.atm.exception.InsufficientBalanceException;
import com.atm.atm.exception.InvalidAmountException;
import com.atm.atm.exception.InvalidCredentialsException;
import com.atm.atm.jpa.AccountRepository;
import com.atm.atm.model.Account;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    UserService userService;

    @Autowired
    AccountRepository accountRepository;

    public BigDecimal retrieveBalance(long userId, String pin) throws InvalidCredentialsException {
        if (userService.verifyCredentials(userId,pin)) {
            Optional<Account> account = userService.retrieveAccount(userId, pin);
            if (account.isPresent()) {
                return account.get().getBalance();
            }
        }
        throw new InvalidCredentialsException(userId);
    }

    public BigDecimal depositMoney(Long userId, String pin, BigDecimal amount) throws InvalidCredentialsException, InvalidAmountException {
        Optional<Account> account = userService.retrieveAccount(userId, pin);
        if (account.isEmpty()){
            logger.error("Invalid credentials provided while attempting to deposit money.");
            throw new InvalidCredentialsException(userId);
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0){
            logger.error("Amount to deposit was negative. Amount: $" + amount);
            throw new InvalidAmountException(userId, amount);
        }

        amount = amount.setScale(2);

        BigDecimal newAmount = retrieveBalance(userId, pin).add(amount);
        account.get().setBalance(newAmount);
        accountRepository.save(account.get());
        return newAmount;
    }

    public BigDecimal withdrawMoney(long userId, String pin, BigDecimal amount) throws InvalidCredentialsException,
            InvalidAmountException, InsufficientBalanceException {
        Optional<Account> account = userService.retrieveAccount(userId,pin);
        if (account.isEmpty()){
            logger.error("Invalid credentials provided while attempting to withdraw money.");
            throw new InvalidCredentialsException(userId);
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0){
            logger.error("Amount to withdraw was negative. Amount: $" + amount);
            throw new InvalidAmountException(userId, amount);
        }

        BigDecimal currentBalance = retrieveBalance(userId, pin);
        amount = amount.setScale(2);
        BigDecimal newAmount = currentBalance.subtract(amount);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0){
            logger.error("Cannot withdraw more than your current balance. Current Balance: $" + currentBalance + ". Withdraw amount: $" + amount);
            throw new InsufficientBalanceException(currentBalance, amount);
        }
        Account account1 = account.get();
        account1.setBalance(newAmount);
        accountRepository.save(account1);
        return newAmount;
    }
}
