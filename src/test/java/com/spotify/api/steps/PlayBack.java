package com.spotify.api.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spotify.api.pojos.*;
import com.spotify.api.utils.GlobalDataUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.spotify.api.utils.ApiUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static com.spotify.api.payloads.PayLoads.*;

@Slf4j (topic = "User Current PlayBack")
public class PlayBack {

    GlobalDataUtils globalData = GlobalDataUtils.get();

    @Given("a device is available")
    public void aDeviceIsAvailable() {
        get("/me/player/devices");
        getResponse().assertThat()
                .body("devices",is(notNullValue()));
        String deviceName = StreamSupport.stream(extractAsArrayNode("devices").spliterator(),false)
                                     .filter(device-> device.get("is_active").asBoolean())
                                     .map(device->device.get("name").asText()).collect(Collectors.joining());
        log.info("Active device found - "+deviceName);

    }

    @Then("play request for the searched album")
    public void playRequestForTheSearchedAlbum() {
        Object payload = playbackPayload(globalData.getAlbum_uri(),0,0);
        put("/me/player/play",payload,204);
    }

    @And("retrieve current playback details")
    public void retrieveCurrentPlaybackDetails() {
        try {
            Thread.sleep(500);
            get("/me/player");
            JsonNode album = extractAsJsonNode("item.album");
            globalData.setAlbum_name(album.get("name").asText());
            globalData.setAlbum_artist(album.get("artists").get(0).get("name").asText());
        }catch (Exception e){
            apiWait(500);
            get("/me/player");
        }

        JsonNode album = extractAsJsonNode("item.album");
        globalData.setAlbum_name(album.get("name").asText());
        globalData.setAlbum_artist(album.get("artists").get(0).get("name").asText());
        globalData.setPlayBackDetails(getResponse().extract().response().as(PlayBackDetails.class));
    }

    @Then("playback details match expected")
    public void playbackDetailsMatchExpected(Map<String,String> dataTable) {
        assertThat(globalData.getAlbum_name(),equalTo(dataTable.get("album")));
        assertThat(globalData.getAlbum_artist(),equalTo(dataTable.get("artist")));
    }

    @And("play request for existing track")
    public void playRequestForExistingTrack() {
       Object payload = playbackPayload("spotify:album:7vus4Q8r5DS2Dl1JClxEsA",0,0);
        put("/me/player/play",payload,204);
    }

    @And("retrieve available devices")
    public void retrieveAvailableDevices() {
        get("/me/player/devices");
    }

    @Then("active device matches expected")
    public void activeDeviceMatchesExpected() {
        getResponse().extract().jsonPath().getList("devices",Device.class)
                .stream().filter(device->!device.is_active()).forEach(device->{
                    assertThat(device.getId(),equalTo(globalData.getPlayBackDetails().getDevice().getId()));
                    assertThat(device.is_active(),is(globalData.getPlayBackDetails().getDevice().is_active()));
                    assertThat(device.is_private_session(),is(globalData.getPlayBackDetails().getDevice().is_private_session()));
                    assertThat(device.is_restricted(),is(globalData.getPlayBackDetails().getDevice().is_restricted()));
                    assertThat(device.getName(),equalTo(globalData.getPlayBackDetails().getDevice().getName()));
                    assertThat(device.getType(),equalTo(globalData.getPlayBackDetails().getDevice().getType()));
                    assertThat(device.getVolume_percent(),is(globalData.getPlayBackDetails().getDevice().getVolume_percent()));
        });
    }

    @And("retrieve currently playing track")
    public void retrieveCurrentlyPlayingTrack() {
        apiWait(250);
        get("/me/player/currently-playing");
        globalData.setTrack_name(getResponse().extract().response().path("item.name"));
    }

    @Then("current track album details match expected")
    public void currentTrackAlbumDetailsMatchExpected() {
        ObjectMapper mapper = new ObjectMapper();
        Album albumResponse = mapper.convertValue(getResponse().extract().response().path("item.album"), Album.class);
        Album albumPlayBack = mapper.convertValue(globalData.getPlayBackDetails().getItem().get("album"), Album.class);
        assertThat(albumPlayBack.getAlbum_type(),equalTo(albumResponse.getAlbum_type()));
        assertThat(albumPlayBack.getId(),equalTo(albumResponse.getId()));
        assertThat(albumPlayBack.getName(),equalTo(albumResponse.getName()));
        assertThat(albumPlayBack.getUri(),equalTo(albumResponse.getUri()));
    }

    @Then("current track artist details match expected")
    public void currentTrackArtistDetailsMatchExpected() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode artistPlayBack = null;
        try {
            artistPlayBack = mapper.treeToValue(globalData.getPlayBackDetails().getItem().get("artists"),ArrayNode.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        ArrayNode artistResponse = extractAsArrayNode("item.artists");
        assertThat(artistPlayBack,equalTo(artistResponse));
    }

    @And("transfer playback to device to {string}")
    public void transferPlaybackToDeviceTo(String deviceName) {
        String id = StreamSupport.stream(extractAsArrayNode("devices").spliterator(),false)
                .filter(device->device.get("name").asText().equals(deviceName))
                .map(device->device.get("id").asText()).collect(Collectors.joining());

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.createObjectNode();
        ArrayNode device_ids = payload.putArray("device_ids");
        device_ids.add(id);
        payload.put("play",true);
        put("/me/player",payload,204);
    }

    @Then("playback should start playing on {string}")
    public void playbackShouldStartPlayingOn(String deviceName) {
        get("/me/player/devices");
        String currentDeviceName = StreamSupport.stream(extractAsArrayNode("devices").spliterator(),false)
                .filter(device->device.get("is_active").asBoolean())
                .map(device->device.get("name").asText()).collect(Collectors.joining());
        assertThat(currentDeviceName,equalTo(deviceName));
    }

    @And("play request for playlist offset track by {int}")
    public void playRequestForPlaylistOffsetTrackBy(int offset) {
        Object payload = playbackPayload("spotify:playlist:7DoNL8LONH7LODhkjkBYHU",offset,0);
        put("/me/player/play",payload,204);
    }

    @Then("verify current track details match expected")
    public void verifyCurrentTrackDetailsMatchExpected(Map<String,Object> dataTable) {
        ObjectMapper mapper = new ObjectMapper();
        Track track = mapper.convertValue(getResponse().extract().response().path("item"),Track.class);

        assertThat(track.getName(),equalTo(dataTable.get("track")));
        assertThat(String.valueOf(track.getTrack_number()),equalTo(String.valueOf(dataTable.get("track_number"))));
        assertThat(track.getArtists()[0].getName(),equalTo(dataTable.get("artist1")));
        assertThat(track.getArtists()[1].getName(),equalTo(dataTable.get("artist2")));
        assertThat(track.getAlbum().getName(),equalTo(dataTable.get("album_name")));
    }

    @And("skip to position {int} seconds in currently playing track")
    public void skipToPositionSecondsInCurrentlyPlayingTrack(int seconds) {
        int milli = seconds*1000;
        globalData.setCurrentPlayPosition(milli);
        Map<String,Object> query = new HashMap<>();
        query.put("position_ms",milli);
        setQueryParam(query);
        put("/me/player/seek",204);
        resetRequestSpec();
    }

    @Then("verify current track play position match expected")
    public void verifyCurrentTrackPlayPositionMatchExpected() {
        getResponse().assertThat()
                .body("progress_ms",greaterThanOrEqualTo(globalData.getCurrentPlayPosition()))
                .body("progress_ms",lessThanOrEqualTo(globalData.getCurrentPlayPosition()+2000));
    }


    @And("skip to position greater than the length of the track")
    public void skipToPositionGreaterThanTheLengthOfTheTrack() {
        int trackLength = getResponse().extract().response().path("item.duration_ms");
        Map<String,Object> query = new HashMap<>();
        query.put("position_ms",trackLength+1000);
        setQueryParam(query);
        put("/me/player/seek",204);
        resetRequestSpec();
    }

    @Then("verify playBack skipped to next song")
    public void verifyPlayBackSkippedToNextSong() {
        String previousTrack = globalData.getTrack_name();
        retrieveCurrentlyPlayingTrack();
        assertThat(previousTrack,not(equalTo(globalData.getTrack_name())));
    }

    @And("pause playBack")
    public void pausePlayBack() {
        put("/me/player/pause",204);
    }

    @Then("verify playBack is paused")
    public void verifyPlayBackIsPaused() {
        getResponse().assertThat()
                .body("is_playing",is(false));
    }

    @And("skip playBack to {string} track")
    public void skipPlayBackToTrack(String direction) {
        post("/me/player/"+direction,204);
    }

    @Then("verify current track is {string}")
    public void verifyCurrentTrackIs(String name) {
        getResponse().assertThat()
                .body("item.name",equalTo(globalData.getTrack_name()));
    }


    @And("set repeat Mode on {string}")
    public void setRepeatModeOn(String mode) {
        Map<String,Object> query = new HashMap<>();
        query.put("state",mode);
        setQueryParam(query);
        put("/me/player/repeat",204);
        resetRequestSpec();
    }

    @Then("verify current playback repeat_state {string}")
    public void verifyCurrentPlaybackRepeat_state(String mode) {
        getResponse().assertThat()
                .body("repeat_state",equalTo(mode));
    }

    @And("set playBack volume to {int}%")
    public void setPlayBackVolumeTo(int vol) {
        Map<String,Object> query = new HashMap<>();
        query.put("volume_percent",vol);
        setQueryParam(query);
        put("/me/player/volume",204);
        resetRequestSpec();
    }

    @Then("verify current playBack volume is {int}%")
    public void verifyCurrentPlayBackVolumeIs(int vol) {
        getResponse().assertThat()
                .body("device.volume_percent",is(vol));
    }


    @And("toggle shuffle mode to {string}")
    public void toggleShuffleModeTo(String mode) {
        Map<String,Object> query = new HashMap<>();
        query.put("state",mode);
        setQueryParam(query);
        put("/me/player/shuffle",204);
        resetRequestSpec();
    }

    @Then("verify current playBack shuffle mode is {string}")
    public void verifyCurrentPlayBackShuffleModeIs(String mode) {
        getResponse().assertThat()
                .body("shuffle_state",is(Boolean.valueOf(mode)));
    }

    @And("add item to queue")
    public void addItemToQueue() {
        Map<String,Object> query = new HashMap<>();
        query.put("uri","spotify:track:3jM12sAVBL1xjzqbavzQDj");
        setQueryParam(query);
        post("/me/player/queue",204);
        resetRequestSpec();
    }
}
