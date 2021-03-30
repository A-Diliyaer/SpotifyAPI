package com.spotify.api.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.api.pojos.Album;
import com.spotify.api.pojos.Artist;
import com.spotify.api.pojos.PlayList;
import com.spotify.api.pojos.Track;
import com.spotify.api.utils.GlobalDataUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

import static com.spotify.api.utils.ApiUtils.*;
import static com.spotify.api.utils.ApiUtils.resetRequestSpec;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class Search {
    GlobalDataUtils globalData = GlobalDataUtils.get();

    @And("search request for album {string}")
    public void searchRequestForAlbum(String albumName) {
        String q = "track:"+albumName;
        Map<String,Object> query = new HashMap<>();
        query.put("q",q);
        query.put("type","track");
        query.put("limit",1);
        setQueryParam(query);
        get("/search");
        String album_uri = getResponse().extract()
                .jsonPath().getString("tracks.items[0].album.uri");
        globalData.setAlbum_uri(album_uri);
        resetRequestSpec();
    }

    @Given("search for artist by name {string}")
    public void searchForArtistByName(String name) {
        Map<String,Object> query = new HashMap<>();
        query.put("q",name);
        query.put("type","artist");
        setQueryParam(query);
        get("/search");
        resetRequestSpec();
        globalData.setSearchArtistName(name);
    }

    @And("retrieve artist from the result")
    public void retrieveArtistFromTheResult() {
        globalData.setArtist(
                new ObjectMapper()
                        .convertValue(StreamSupport.stream(extractAsArrayNode("artists.items").spliterator(),false)
                                .filter(each->each.get("name").asText().equals(globalData.getSearchArtistName()))
                                .findFirst().get(),Artist.class));
    }

    @Then("verify artist details match expected")
    public void verifyArtistDetailsMatchExpected() {
        JsonNode node = extractAsJsonNode();
        assertThat(globalData.getArtist().getName(),equalTo(node.get("name").asText()));
        assertThat(globalData.getArtist().getType(),equalTo(node.get("type").asText()));
        assertThat(globalData.getArtist().getId(),equalTo(node.get("id").asText()));
    }

    @Given("search for track by name {string} by artist {string} in album {string}")
    public void searchForTrackByNameByArtistInAlbum(String track, String artist, String album) {
        Map<String,Object> query = new HashMap<>();
        String q = "track:"+track+" artist:"+artist+" album:"+album;
        query.put("q",q);
        query.put("type","track");
        setQueryParam(query);
        get("/search");
        resetRequestSpec();
        globalData.setSearchTrackDetails(Arrays.asList(track,artist,album));
    }

    @And("retrieve track from the result")
    public void retrieveTrackFromTheResult() {
        globalData.setSearchedTrack(
                new ObjectMapper()
                        .convertValue(StreamSupport.stream(extractAsArrayNode("tracks.items").spliterator(),false)
                                .filter(each->each.get("name").asText().equals(globalData.getSearchTrackDetails().get(0)))
                                .filter(each->each.get("album").get("name").asText().equals(globalData.getSearchTrackDetails().get(2)))
                                .findFirst().get(), Track.class));
        globalData.setTrack_id(globalData.getSearchedTrack().getId());
    }

    @Then("verify searched track details match expected")
    public void verifySearchedTrackDetailsMatchExpected() {
        assertThat(globalData.getTrack().getName(),equalTo(globalData.getSearchedTrack().getName()));
        assertThat(globalData.getTrack().getAlbum().getName(),equalTo(globalData.getSearchedTrack().getAlbum().getName()));
        String artist = globalData.getSearchedTrack().getArtists()[0].getName();
        assertThat(artist,equalTo(globalData.getSearchTrackDetails().get(1)));
    }


    @Given("search for album by name {string}")
    public void searchForAlbumByName(String name) {
        Map<String,Object> query = new HashMap<>();
        String q = "album:"+name;
        query.put("q",q);
        query.put("type","album");
        setQueryParam(query);
        get("/search");
        resetRequestSpec();
        globalData.setSearchedAlbumName(name);
    }

    @And("retrieve album from the result")
    public void retrieveAlbumFromTheResult() {
        globalData.setSearchedAlbum(
                new ObjectMapper()
                        .convertValue(StreamSupport.stream(extractAsArrayNode("albums.items").spliterator(),false)
                                .filter(each->each.get("name").asText().equals(globalData.getSearchedAlbumName()))
                                .findFirst().get(), Album.class));
    }


    @Then("verify searched album details match expected")
    public void verifySearchedAlbumDetailsMatchExpected() {
        JsonNode album = getResponse().extract().response().as(JsonNode.class);
        assertThat(album.get("name").asText(),equalTo(globalData.getSearchedAlbumName()));
        assertThat(album.get("artists").get(0).get("name").asText(),equalTo(globalData.getSearchedAlbum().getArtists()[0].getName()));
    }

    @Given("search for playlist by name {string}")
    public void searchForPlaylistByName(String name) {
        Map<String,Object> query = new HashMap<>();
        String q = "playlist:"+name;
        query.put("q",q);
        query.put("type","playlist");
        setQueryParam(query);
        get("/search");
        resetRequestSpec();
        globalData.setPlayListName(name);
    }

    @And("retrieve playlist from the result")
    public void retrievePlaylistFromTheResult() {
        globalData.setPlaylist(
                new ObjectMapper()
                        .convertValue(StreamSupport.stream(extractAsArrayNode("playlists.items").spliterator(),false)
                                .filter(each->each.get("name").asText().equals(globalData.getPlayListName()))
                                .findFirst().get(), PlayList.class));
    }

    @Then("verify searched playlist details match expected")
    public void verifySearchedPlaylistDetailsMatchExpected() {
        JsonNode playlist = getResponse().extract().response().as(JsonNode.class);
        assertThat(playlist.get("name").asText(),equalTo(globalData.getPlaylist().getName()));
        assertThat(playlist.get("description").asText(),equalTo(globalData.getPlaylist().getDescription()));
    }
}
