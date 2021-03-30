package com.spotify.api.payloads;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.spotify.api.utils.GlobalDataUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PayLoads {

    static GlobalDataUtils globalData = GlobalDataUtils.get();

    public static ObjectNode playbackPayload(String context_uri,int position,int position_ms){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.createObjectNode();
        payload.put("context_uri",context_uri);
        ObjectNode offset = mapper.createObjectNode();
        offset.put("position",position);
        payload.set("offset",offset);
        payload.put("position_ms",position_ms);
        return payload;
    }

    public static ObjectNode playListPayload(String state,String name){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.createObjectNode();
        boolean is_public = false;
        if (state.equals("public")) is_public = true;
        payload.put("name",name);
        payload.put("public",is_public);
        payload.put("collaborative",false);
        payload.put("description","this is a practice playlist");
        return payload;
    }

    public static ObjectNode playListItems(List<String> tracks){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode payload = mapper.createObjectNode();
        ArrayNode uris = payload.putArray("uris");
        for (String each:tracks){
            uris.add(each);
        }
        payload.put("position",0);
        return payload;
    }

    public static void playListNewOrder(Map<String,Integer> dataTable){
        int start = dataTable.get("range_start");
        int length = start+dataTable.get("range_length");
        List<String> values = globalData.getPlayListExistingOrder();
        List<String> temp = new ArrayList<>();
        for (int i = start; i <length; i++) {
           temp.add(values.get(i));
        }
        int index = dataTable.get("insert_before");
        values.addAll(index,temp);
        for (int i = start; i < length; i++) {
            values.remove(0);
        }
        globalData.setPlayListNewOrder(values);
    }
}
