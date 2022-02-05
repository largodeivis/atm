package com.atm.atm.controller;

import com.atm.atm.exception.InsufficientBalanceException;
import com.atm.atm.exception.InvalidAmountException;
import com.atm.atm.exception.InvalidCredentialsException;
import com.atm.atm.model.AccountTransaction;
import com.atm.atm.service.AccountService;
import com.atm.atm.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void retrieveBalanceReturnsBalance() throws Exception {
        when(accountService.retrieveBalance(123456789, "1234")).thenReturn(new BigDecimal("100.00"));
        this.mockMvc.perform(get("/user/account?userid=123456789&pin=1234")).andDo(print())
                .andExpect(content().string(containsString("User ID: 123456789\nBalance: $100")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void givenBadCredentalsRetrieveBalanceThrowsInvalidCredentialsException() throws Exception {
        when(accountService.retrieveBalance(123456789, "123")).thenThrow(new InvalidCredentialsException(123456789L));
        this.mockMvc.perform(get("/user/account?userid=123456789&pin=123")).andDo(print())
                .andExpect(content().string(containsString("Invalid Credentials provided for user: 123456789")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void depositMoneyReturnsNewBalance() throws Exception {
        when(accountService.depositMoney(123456789L, "1234", new BigDecimal("250.00"))).thenReturn(new BigDecimal("350.00"));

        AccountTransaction accountTransaction = new AccountTransaction(new BigDecimal("250.00"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String accountTransactionJson = objectWriter.writeValueAsString(accountTransaction);

        this.mockMvc.perform(post("/account/deposit?userid=123456789&pin=1234")
                .contentType(MediaType.APPLICATION_JSON).content(accountTransactionJson)).andDo(print())
                .andExpect(content().string(containsString("UserId: 123456789\nNew Balance: $350.00")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void depositMoneyWithBadCredentialsThrowsInvalidCredentialsException() throws Exception {
        when(accountService.depositMoney(123456789L, "124", new BigDecimal("250.00"))).thenThrow(new InvalidCredentialsException(123456789L));

        AccountTransaction accountTransaction = new AccountTransaction(new BigDecimal("250.00"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String accountTransactionJson = objectWriter.writeValueAsString(accountTransaction);

        this.mockMvc.perform(post("/account/deposit?userid=123456789&pin=124")
                .contentType(MediaType.APPLICATION_JSON).content(accountTransactionJson)).andDo(print())
                .andExpect(content().string(containsString("Invalid Credentials provided for user: 123456789")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void depositMoneyWithNegativeAmountThrowsInvalidAmountException() throws Exception {
        Long userId = 123456789L;
        BigDecimal amount = new BigDecimal("-250.00");
        when(accountService.depositMoney(userId, "124", amount)).thenThrow(new InvalidAmountException(userId, amount));

        AccountTransaction accountTransaction = new AccountTransaction(amount);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String accountTransactionJson = objectWriter.writeValueAsString(accountTransaction);

        this.mockMvc.perform(post("/account/deposit?userid=123456789&pin=124")
                .contentType(MediaType.APPLICATION_JSON).content(accountTransactionJson)).andDo(print())
                .andExpect(content().string(containsString("Invalid Amount provided for user.\nUser: " + userId + "\nAmount:" + amount)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void withdrawMoneyReturnsNewBalance() throws Exception {
        when(accountService.withdrawMoney(123456789L, "1234", new BigDecimal("50.00"))).thenReturn(new BigDecimal("50.00"));

        AccountTransaction accountTransaction = new AccountTransaction(new BigDecimal("50.00"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String accountTransactionJson = objectWriter.writeValueAsString(accountTransaction);

        this.mockMvc.perform(post("/account/withdraw?userid=123456789&pin=1234")
                .contentType(MediaType.APPLICATION_JSON).content(accountTransactionJson)).andDo(print())
                .andExpect(content().string(containsString("UserId: 123456789\nNew Balance: $50.00")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void withdrawMoneyWithBadCredentialsThrowsInvalidCredentialsException() throws Exception {
        when(accountService.withdrawMoney(123456789L, "124", new BigDecimal("50.00"))).thenThrow(new InvalidCredentialsException(123456789L));

        AccountTransaction accountTransaction = new AccountTransaction(new BigDecimal("50.00"));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String accountTransactionJson = objectWriter.writeValueAsString(accountTransaction);

        this.mockMvc.perform(post("/account/withdraw?userid=123456789&pin=124")
                .contentType(MediaType.APPLICATION_JSON).content(accountTransactionJson)).andDo(print())
                .andExpect(content().string(containsString("Invalid Credentials provided for user: 123456789")))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void withdrawMoneyWithNegativeAmountThrowsInvalidAmountException() throws Exception {
        Long userId = 123456789L;
        BigDecimal amount = new BigDecimal("-250.00");
        when(accountService.withdrawMoney(userId, "1234", amount)).thenThrow(new InvalidAmountException(userId, amount));

        AccountTransaction accountTransaction = new AccountTransaction(amount);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String accountTransactionJson = objectWriter.writeValueAsString(accountTransaction);

        this.mockMvc.perform(post("/account/withdraw?userid=123456789&pin=1234")
                .contentType(MediaType.APPLICATION_JSON).content(accountTransactionJson)).andDo(print())
                .andExpect(content().string(containsString("Invalid Amount provided for user.\nUser: " + userId + "\nAmount:" + amount)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void withdrawMoneyWithInsufficientFundsThrowsInsufficientBalanceException() throws Exception {
        Long userId = 123456789L;
        BigDecimal amount = new BigDecimal("250.00");
        BigDecimal balance = new BigDecimal("100.00");
        when(accountService.withdrawMoney(userId, "1234", amount)).thenThrow(new InsufficientBalanceException(balance, amount));

        AccountTransaction accountTransaction = new AccountTransaction(amount);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String accountTransactionJson = objectWriter.writeValueAsString(accountTransaction);

        this.mockMvc.perform(post("/account/withdraw?userid=123456789&pin=1234")
                .contentType(MediaType.APPLICATION_JSON).content(accountTransactionJson)).andDo(print())
                .andExpect(content().string(containsString("There are insufficient funds to withdraw the desired amount." +
                        "\nCurrent Balance: $" + balance + "\nWithdraw Amount: $" + amount)))
                .andExpect(status().is2xxSuccessful());
    }
}
