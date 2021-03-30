package com.spotify.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayBackDetails {
    private Device device;
    private boolean shuffle_state;
    private String repeat_state;
    @JsonIgnore
    private Integer timestamp;
    private ObjectNode context;
    private Integer progress_ms;
    private ObjectNode item;
    private String currently_playing_type;
    private Actions actions;
    private boolean is_playing;
}
