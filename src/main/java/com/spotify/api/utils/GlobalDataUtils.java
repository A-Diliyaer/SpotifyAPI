package com.spotify.api.utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spotify.api.pojos.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class GlobalDataUtils {
    private static GlobalDataUtils instance;
    private String album_uri;
    private String album_name;
    private String album_artist;
    private PlayBackDetails playBackDetails;
    private Integer currentPlayPosition;
    private String track_name;
    private String track_id;
    private Track track;
    private User me;
    private String playListName;
    private boolean playListIsPublic;
    private PlayList playlist;
    private ObjectNode playListUpdate;
    private String playListSnapShot;
    private List<String> playListItems;
    private List<String> playListNewOrder;
    private List<String> playListExistingOrder;
    private List<String> deletedItems;
    private String searchArtistName;
    private Artist artist;
    private List<String> searchTrackDetails;
    private Track searchedTrack;
    private Album searchedAlbum;
    private String searchedAlbumName;
    private PlayList searchedPlayList;

    private GlobalDataUtils(){};

    public static GlobalDataUtils get(){
        if (instance==null){
            instance = new GlobalDataUtils();
        }
        return instance;
    }

}
