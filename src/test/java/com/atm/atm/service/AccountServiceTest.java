package com.atm.atm.service;

import com.atm.atm.exception.InsufficientBalanceException;
import com.atm.atm.exception.InvalidAmountException;
import com.atm.atm.exception.InvalidCredentialsException;
import com.atm.atm.jpa.AccountRepository;
import com.atm.atm.jpa.UserRepository;
import com.atm.atm.model.Account;
import com.atm.atm.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    private UserService userService;

    private AccountService accountService;

    Account account1 = new Account("1000.00");
    User user1 = new User(1234, "Freddy", "1111", account1);

    Account account2 = new Account("200.00");
    User user2 = new User(4321, "Daphne", "2222", account2);

    List<User> userList;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository, accountRepository);
        accountService = new AccountService(userService, accountRepository);
        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
    }

    @AfterEach
    public void tearDown() {
        userService = null;
        userList = null;
        user1 = null;
        user2 = null;
        account1 = null;
        account2 = null;
    }

    @Test
    void retriveBalanceReturnsBalance() throws InvalidCredentialsException {
        long userId = 1234;
        String pin = "1111";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        BigDecimal balance = accountService.retrieveBalance(userId, pin);
        assertEquals(balance, new BigDecimal("1000.00"));
    }

    @Test
    void retriveBalanceInvalidCredentialsThrowsException() {
        long userId = 1234;
        String pin = "2222";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        Exception thrown = assertThrows(InvalidCredentialsException.class, () -> accountService.retrieveBalance(userId, pin));
        assertEquals("Invalid Credentials provided for user: " + userId + ".", thrown.getMessage());
    }

    @Test
    void depositMoneyReturnsNewBalance() throws InvalidCredentialsException, InvalidAmountException {
        long userId = 1234;
        String pin = "1111";
        BigDecimal amount = new BigDecimal("30.15");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        BigDecimal balance = accountService.depositMoney(userId, pin, amount);
        assertEquals(balance, new BigDecimal("1030.15"));
    }

    @Test
    void depositMoneyInvalidCredentialsThrowsException() {
        long userId = 1234;
        String pin = "2222";
        BigDecimal amount = new BigDecimal("30.15");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        Exception thrown = assertThrows(InvalidCredentialsException.class, () -> accountService.depositMoney(userId, pin, amount));
        assertEquals("Invalid Credentials provided for user: " + userId + ".", thrown.getMessage());
    }

    @Test
    void depositMoneyInvalidAmountThrowsException() {
        long userId = 1234;
        String pin = "1111";
        BigDecimal amount = new BigDecimal("-100.00");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        Exception thrown = assertThrows(InvalidAmountException.class, () -> accountService.depositMoney(userId, pin, amount));
        assertEquals("Invalid Amount provided for user.\nUser: " + userId + "\nAmount:" + amount, thrown.getMessage());
    }

    @Test
    void withdrawMoneyReturnsNewBalance() throws InvalidCredentialsException, InvalidAmountException, InsufficientBalanceException {
        long userId = 1234;
        String pin = "1111";
        BigDecimal amount = new BigDecimal("400.00");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        BigDecimal balance = accountService.withdrawMoney(userId, pin, amount);
        assertEquals(balance, new BigDecimal("600.00"));
    }

    @Test
    void withdrawMoneyInvalidCredentialsThrowsException() {
        long userId = 1234;
        String pin = "2222";
        BigDecimal amount = new BigDecimal("400.00");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        Exception thrown = assertThrows(InvalidCredentialsException.class, () -> accountService.withdrawMoney(userId, pin, amount));
        assertEquals("Invalid Credentials provided for user: " + userId + ".", thrown.getMessage());
    }

    @Test
    void withdrawMoneyInvalidAmountThrowsException() {
        long userId = 1234;
        String pin = "1111";
        BigDecimal amount = new BigDecimal("-400.00");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        Exception thrown = assertThrows(InvalidAmountException.class, () -> accountService.withdrawMoney(userId, pin, amount));
        assertEquals("Invalid Amount provided for user.\nUser: " + userId + "\nAmount:" + amount, thrown.getMessage());
    }

    @Test
    void withdrawMoneyInsufficientAmountThrowsException() throws InvalidCredentialsException {
        long userId = 1234;
        String pin = "1111";
        BigDecimal amount = new BigDecimal("2000.00");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user1));
        BigDecimal balance = accountService.retrieveBalance(userId, pin);
        Exception thrown = assertThrows(InsufficientBalanceException.class, () -> accountService.withdrawMoney(userId, pin, amount));
        assertEquals("There are insufficient funds to withdraw the desired amount." +
                "\nCurrent Balance: $" + balance + "\nWithdraw Amount: $" + amount, thrown.getMessage());
    }

}
