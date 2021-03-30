package com.spotify.api.steps;

import com.spotify.api.utils.GlobalDataUtils;
import io.cucumber.java.en.And;

import static com.spotify.api.utils.ApiUtils.*;
import static com.spotify.api.utils.ApiUtils.resetRequestSpec;
import static com.spotify.api.payloads.PayLoads.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class Artists {
    GlobalDataUtils globalData = GlobalDataUtils.get();

    @And("retrieve artist by id")
    public void retrieveArtistById() {
        get("/artists/"+globalData.getArtist().getId());
    }
}
