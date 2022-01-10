package com.atm.atm.controller;

import com.atm.atm.AtmApplication;
import org.json.JSONException;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Arrays;

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
        String retrieveAccountUrl = url + port + "/users/2/account";

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(retrieveAccountUrl, HttpMethod.GET, entity, String.class);

        String expected = "{id:1,balance:100.00}";

        JSONAssert.assertEquals(expected, response.getBody(), false);

    }

}
