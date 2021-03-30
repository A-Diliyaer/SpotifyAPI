package com.spotify.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {
    private Followers followers;
    private String[] genres;
    private String href;
    private String id;
    private String name;
    private Integer popularity;
    private String type;
    private String uri;
}
