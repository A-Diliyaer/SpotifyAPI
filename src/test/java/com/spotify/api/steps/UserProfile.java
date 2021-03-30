package com.spotify.api.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.api.pojos.User;
import com.spotify.api.utils.GlobalDataUtils;
import io.cucumber.java.en.Given;

import static com.spotify.api.utils.ApiUtils.*;

public class UserProfile {
    GlobalDataUtils globalData = GlobalDataUtils.get();

    @Given("retrieve current user profile")
    public void retrieveCurrentUserProfile() {
        get("/me");
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(getResponse().extract().response().path(""), User.class);
        globalData.setMe(user);
    }
}
