package com.atm.atm.controller;

import com.atm.atm.exception.InvalidCredentialsException;
import com.atm.atm.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    public void givenBadCredentalsRetrieveBalanceThrowsException() throws Exception {
        when(accountService.retrieveBalance(123456789, "123")).thenThrow(new InvalidCredentialsException(123456789L));
        this.mockMvc.perform(get("/user/account?userid=123456789&pin=123")).andDo(print())
                .andExpect(content().string(containsString("Invalid Credentials provided for user: 123456789")))
                .andExpect(status().is4xxClientError());
    }




}
