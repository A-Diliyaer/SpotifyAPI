package com.spotify.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioFeatures {
    private Float danceability;
    private Float energy;
    private Integer key;
    private Float loudness;
    private Integer mode;
    private Float speechiness;
    private Float acousticness;
    private Float instrumentalness;
    private Float liveness;
    private Float tempo;
    private String type;
    private String id;
    private String uri;
    private String track_href;
    private String analysis_url;
    private Integer duration_ms;
    private Integer time_signature;

}
