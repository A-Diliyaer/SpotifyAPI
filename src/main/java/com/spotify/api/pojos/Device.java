package com.spotify.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {
    private String id;
    private boolean is_active;
    private boolean is_private_session;
    private boolean is_restricted;
    private String name;
    private String type;
    private Integer volume_percent;

}
