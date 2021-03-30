package com.spotify.api.steps;

import com.spotify.api.utils.GlobalDataUtils;
import io.cucumber.java.en.And;

import static com.spotify.api.utils.ApiUtils.*;
import static com.spotify.api.utils.ApiUtils.resetRequestSpec;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class Albums {

    GlobalDataUtils globalData = GlobalDataUtils.get();

    @And("retrieve album by id")
    public void retrieveAlbumById() {
        get("/albums/"+globalData.getSearchedAlbum().getId());
    }

    @And("retrieve album tracks by id")
    public void retrieveAlbumTracksById() {
        get("/albums/"+globalData.getSearchedAlbum().getId()+"/tracks");
    }
}
