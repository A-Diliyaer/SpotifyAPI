package com.spotify.api.steps;


import com.spotify.api.utils.ConfigurationReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;

import java.util.Base64;

import static com.spotify.api.utils.ApiUtils.*;
import static io.restassured.RestAssured.*;

public class Hooks {

    @Before
    public void setup(){
        String clientIdSecret = ConfigurationReader.getProperty("client_id")+":"+ConfigurationReader.getProperty("client_secret");
        String auth = "Basic "+ Base64.getEncoder().encodeToString(clientIdSecret.getBytes());
        setAccessToken(auth);
        baseURI = ConfigurationReader.getProperty("base_url");
    }

    @After
    public void destroy(){
        reset();
    }
}
