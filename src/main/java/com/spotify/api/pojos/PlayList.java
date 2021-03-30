package com.spotify.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayList {
    private boolean collaborative;
    private String description;
    private Followers followers;
    @JsonIgnore
    private String href;
    private String id;
    private String name;
    private User owner;
    @JsonProperty("public")
    private boolean is_public;
    private String snapshot_id;
    private JsonNode tracks;
    private String type;
    private String uri;

}
