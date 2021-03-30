package com.spotify.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String country;
    private String display_name;
    private String email;
    private Followers followers;
    private String href;
    private String id;
    private String type;
    private String uri;
}
