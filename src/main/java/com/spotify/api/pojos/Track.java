package com.spotify.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {
    private Album album;
    private Artist[] artists;
    private String[] available_markets;
    private Integer disc_number;
    private Integer duration_ms;
    private boolean explicit;
    private String href;
    private String id;
    private boolean is_local;
    private boolean is_playable;
    private String name;
    private Integer popularity;
    private String preview_url;
    private Integer track_number;
    private String type;
    private String uri;

}
