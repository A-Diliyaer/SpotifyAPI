package com.spotify.api.steps;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.spotify.api.pojos.PlayList;
import com.spotify.api.pojos.Track;
import com.spotify.api.utils.GlobalDataUtils;
import io.cucumber.java.en.And;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.spotify.api.payloads.PayLoads.*;
import static com.spotify.api.utils.ApiUtils.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class PlayLists {
    GlobalDataUtils globalData = GlobalDataUtils.get();

    @And("create {string} playlist {string}")
    public void createPlaylist(String state, String name) {
        ObjectNode payload = playListPayload(state,name);
        post("/users/"+globalData.getMe().getId()+"/playlists",payload,201);
        globalData.setPlayListName(name);
        if (state.equals("public")) globalData.setPlayListIsPublic(true);
        if (state.equals("private")) globalData.setPlayListIsPublic(false);
        globalData.setPlaylist(getResponse().extract().response().as(PlayList.class));
    }

    @Then("verify playlist details match expected")
    public void verifyPlaylistDetailsMatchExpected() {
        getResponse().assertThat()
                .body("name",equalTo(globalData.getPlayListName()))
                .body("owner.display_name",equalTo(globalData.getMe().getDisplay_name()))
                .body("public",is(globalData.isPlayListIsPublic()));
    }

    @And("retrieve list of user playlists")
    public void retrieveListOfUserPlaylists() {
        get("/me/playlists");
    }

    @Then("verify playlist exists in the list")
    public void verifyPlaylistExistsInTheList() {
        StreamSupport.stream(extractAsArrayNode("items").spliterator(),false)
                .filter(playlist->playlist.get("name").equals(globalData.getPlayListName()))
                .forEach(each->assertThat(each,equalTo(globalData.getPlayListName())));
    }

    @And("update playlist details")
    public void updatePlaylistDetails(Map<String,String> dataTable) {
        ObjectNode payload = playListPayload(dataTable.get("state"),dataTable.get("name"));
        payload.put("description",dataTable.get("description"));
        globalData.setPlayListUpdate(payload);
        put("/playlists/"+globalData.getPlaylist().getId(),payload);
    }

    @And("retrieve playlist by id")
    public void retrievePlaylistById() {
        get("/playlists/"+globalData.getPlaylist().getId());
    }

    @Then("verify playlist details are updated as expected")
    public void verifyPlaylistDetailsAreUpdatedAsExpected() {
        getResponse().assertThat()
                .body("name",equalTo(globalData.getPlayListUpdate().get("name").asText()))
                .body("public",is(globalData.getPlayListUpdate().get("public").asBoolean()))
                .body("description",equalTo(globalData.getPlayListUpdate().get("description").asText()));
    }

    @And("add tracks to current playlist by id")
    public void addTracksToCurrentPlaylistById(List<String> tracks) {
        ObjectNode payload = playListItems(tracks);
        post("/playlists/"+globalData.getPlaylist().getId()+"/tracks",payload,201);
        globalData.setPlayListSnapShot(getResponse().extract().response().path("snapshot_id"));
        globalData.setPlayListItems(tracks);
    }

    @Then("verify playlist snapshot_id")
    public void verifyPlaylistSnapshot_id() {
        getResponse().assertThat()
                .body("snapshot_id",equalTo(globalData.getPlayListSnapShot()));
    }

    @Then("verify new items exist in the playlist")
    public void verifyNewItemsExistInThePlaylist() {
        StreamSupport.stream(extractAsArrayNode("tracks.items").spliterator(),false)
                .map(track->track.get("track").get("uri").asText()).collect(Collectors.toList())
                .forEach(uri->assertThat(uri,is(in(globalData.getPlayListItems()))));
    }

    @Given("retrieve existing playlist {string}")
    public void retrieveExistingPlaylist(String playListName) {
        retrieveListOfUserPlaylists();
        List<JsonNode> node =
        StreamSupport.stream(extractAsArrayNode("items").spliterator(),false)
                            .filter(playlist->playlist.get("name").asText().equals(playListName))
                            .collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        PlayList playList = mapper.convertValue(node.get(0),PlayList.class);
        globalData.setPlaylist(playList);
    }

    @And("reorder playlist items with given config")
    public void reorderPlaylistItemsWithGivenConfig(Map<String,Integer> dataTable) {
        retrievePlaylistById();
        List<String> tracks = StreamSupport.stream(extractAsArrayNode("tracks.items").spliterator(),false)
                .map(track->track.get("track").get("name").asText()).collect(Collectors.toList());
        globalData.setPlayListExistingOrder(tracks);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.convertValue(dataTable,ObjectNode.class);
        playListNewOrder(dataTable);
        put("/playlists/"+globalData.getPlaylist().getId()+"/tracks",payload);
    }

    @Then("verify playlist item reorder match expected")
    public void verifyPlaylistItemReorderMatchExpected() {
        List<String> trackNames = StreamSupport.stream(extractAsArrayNode("tracks.items").spliterator(),false)
                .map(track->track.get("track").get("name").asText()).collect(Collectors.toList());

        for (int i = 0; i < trackNames.size(); i++) {
            assertThat(trackNames.get(i),equalTo(globalData.getPlayListNewOrder().get(i)));
        }
    }

    @And("remove below tracks from the playlist")
    public void removeBelowTracksFromThePlaylist(List<String> trackList) {
        addTracksToCurrentPlaylistById(trackList);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.createObjectNode();
        ArrayNode arr = payload.putArray("tracks");
        for (int i = 0; i < trackList.size(); i++) {
            ObjectNode track = mapper.createObjectNode();
            track.put("uri",trackList.get(i));
            arr.add(track);
        }
        globalData.setDeletedItems(trackList);
        delete("/playlists/"+globalData.getPlaylist().getId()+"/tracks",payload);
    }

    @Then("verify deleted tracks do not exist in the playlist")
    public void verifyDeletedTracksDoNotExistInThePlaylist() {
        retrievePlaylistById();
        List<String> trackNames = StreamSupport.stream(extractAsArrayNode("tracks.items").spliterator(),false)
                .map(track->track.get("track").get("name").asText()).collect(Collectors.toList());
        globalData.getDeletedItems().stream().forEach(each->{
            assertThat(each,not(in(trackNames)));
        });
    }
}
