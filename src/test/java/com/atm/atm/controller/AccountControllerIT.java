package com.atm.atm.controller;

import com.atm.atm.AtmApplication;
import org.json.JSONException;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = AtmApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountControllerIT {

    @LocalServerPort
    private int port;

    String url = "http://localhost:";

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Before
    public void before(){
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testRetrieveAccount() throws JSONException {
        String retrieveAccountUrl = url + port + "/user/account?userid=123456789&pin=1234";

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(retrieveAccountUrl, HttpMethod.GET, entity, String.class);

        String expected = "User ID: 123456789\nBalance: $100.00";

        Assertions.assertEquals(expected, response.getBody());

    }

}
