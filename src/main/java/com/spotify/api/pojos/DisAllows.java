package com.spotify.api.pojos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DisAllows {
    private boolean interrupting_playback;
    private boolean pausing;
    private boolean resuming;
    private boolean seeking;
    private boolean skipping_next;
    private boolean skipping_prev;
    private boolean toggling_repeat_context;
    private boolean toggling_repeat_track;
    private boolean toggling_shuffle;
    private boolean transferring_playback;
}
