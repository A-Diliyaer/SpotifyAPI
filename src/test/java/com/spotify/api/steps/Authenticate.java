package com.spotify.api.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import static com.spotify.api.utils.ApiUtils.*;

@Slf4j (topic = "User Authentication")
public class Authenticate {

    @Given("I am authenticated as {string}")
    public void iAmAuthenticatedAs(String role) {
        Assert.assertNotNull("Authentication Token is null",getAccessToken());
        setBaseReqSpec();
        log.info("Request header setting complete");
    }

    @Then("status code is {int}")
    public void statusCodeIs(int statusCode) {
        getResponse().assertThat()
                .statusCode(statusCode);
    }
}
