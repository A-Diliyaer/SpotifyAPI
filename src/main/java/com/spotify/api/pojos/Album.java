package com.spotify.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Album {
    private String album_type;
    private Artist[] artists;
    private String[] available_markets;
    private String[] genres;
    private String id;
    private String label;
    private String name;
    private Integer popularity;
    private String release_date;
    private String release_date_prevision;
    private Integer total_tracks;
    private Track tracks;
    private String type;
    private String uri;
}
