package com.atm.atm.controller;

import com.atm.atm.AtmApplication;
import com.atm.atm.model.Account;
import com.atm.atm.model.User;
import org.json.JSONException;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

@SpringBootTest(classes = AtmApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIT {

    @LocalServerPort
    private int port;

    String url = "http://localhost:" + port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Before
    public void before(){
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testRetrieveAccount() throws JSONException {
        String retrieveAccountUrl = url + "/users/2/account";

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(retrieveAccountUrl, HttpMethod.GET, entity, String.class);

        String expected = "{id:1,balance:100.00}";

        JSONAssert.assertEquals(expected, response.getBody(), false);

    }

//    @Test
//    public void testRetrieveAllUsers() throws JSONException {
//         String retrieveAllUsersUrl = url + "/users/";
//
//        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(retrieveAllUsersUrl, HttpMethod.GET, entity, String.class);
//
//        String expected = "[{id:2,userId:Shaggy,pin:'1234',account:{id:1,balance:100.00}},{id:4,userId:Scooby,pin:'1111',account:{id:3,balance:5050.58}},{id:6,userId:Scrappy,pin:'5555',account:{id:5,balance:7.12}}]";
//
//        JSONAssert.assertEquals(expected, response.getBody(), false);
//
//    }
//
//    @Test
//    public void testCreateUser() throws JSONException {
//        String createUserUrl = url + "/users/create-user";
//
//        Account account = new Account(new BigDecimal("777.77"));
//        User user = new User("Daphne", "7777", account);
//
//        HttpEntity entity = new HttpEntity<User>(user, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(createUserUrl, HttpMethod.POST, entity, String.class);
//
//        String actual = Objects.requireNonNull(response.getHeaders().get(HttpHeaders.LOCATION)).get(0);
//
//        Assertions.assertTrue(actual.contains("/users/"));
//    }

}
