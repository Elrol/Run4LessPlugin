package com.github.elrol.run4lessplugin;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RunData {
    String client = "";
    transient long start;
    Map<String, Integer> bones = new HashMap<>();

    public RunData (String client, String type, int qty){
        this.client = client;
        start = System.currentTimeMillis();
        log.info("Run Start: " + String.valueOf(start));
        bones.put(type, qty);
    }

    public void ran(String type, int qty){
        if(bones.containsKey(type)){
            int q = bones.get(type) - qty;
            if(q <= 0) bones.remove(type);
            else bones.put(type, bones.get(type) - qty);
        }
    }

    public void add(String type, int qty){
        bones.put(type, bones.getOrDefault(type, 0) + qty);
    }

    public boolean check(){
        return bones.isEmpty();
    }
}
