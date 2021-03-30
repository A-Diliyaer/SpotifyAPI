package com.spotify.api.steps;

import com.spotify.api.pojos.AudioFeatures;
import com.spotify.api.pojos.Track;
import com.spotify.api.utils.GlobalDataUtils;
import io.cucumber.java.en.And;

import static com.spotify.api.utils.ApiUtils.*;
public class Tracks {

    GlobalDataUtils globalData = GlobalDataUtils.get();

    @And("retrieve a track by id")
    public void retrieveATrackById() {
        String track_id = globalData.getTrack_id();
        get("/tracks/"+track_id);
        Track track = getResponse().extract().response().as(Track.class);
        globalData.setTrack(track);
    }

    @And("retrieve audio features of a track")
    public void retrieveAudioFeaturesOfATrack() {
        String track_id = globalData.getTrack_id();
        get("/audio-features"+track_id);
        AudioFeatures audio = getResponse().extract().response().as(AudioFeatures.class);
    }

}
