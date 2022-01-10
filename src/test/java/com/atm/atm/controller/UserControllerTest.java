package com.atm.atm.controller;

import com.atm.atm.model.Account;
import com.atm.atm.model.User;
import com.atm.atm.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import java.math.BigDecimal;

@WebMvcTest(value = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void retrieveUserDetails() throws Exception {
        Account account = new Account(new BigDecimal("333.33"));
        User mockUser = new User("Freddy", "3333", account);

        Mockito.when(userService.retrieveUser(0)).thenReturn(Optional.of((mockUser)));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/0/").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "{id:0,userId:Freddy,pin:'3333',account:{id:0,balance:333.33}}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

    }

    @Test
    public void createUser() throws Exception {
        Account account = new Account(new BigDecimal("333.33"));
        User mockUser = new User("Freddy", "3333", account);

        String userJson = "{\"id\":0,\"userId\":\"Freddy\",\"pin\":\"3333\",\"account\":{\"id\":0,\"balance\":333.33}}";
        Mockito.when(userService.createUser(Mockito.any(User.class))).thenReturn(mockUser);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/create-user")
                .accept(MediaType.APPLICATION_JSON).content(userJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        Assertions.assertEquals("http://localhost/users/create-user/0", response.getHeader(HttpHeaders.LOCATION));

    }

}
