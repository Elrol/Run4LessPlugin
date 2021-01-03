package com.github.elrol.run4lessplugin;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class HostData {
    @SerializedName(value = "OSRS Hosts")
    public Host[] OSRS_Hosts;

    public List<String> getUsers(){
        List<String> users = new ArrayList<>();
        if(OSRS_Hosts == null) return users;
        for(Host host : OSRS_Hosts){
            users.add(host.Username);
        }
        return users;
    }

    public static HostData load(String hostJson){
        Gson gson = new Gson();
        HostData hostData = new HostData();
        if(hostJson == null || hostJson.isEmpty()) return hostData;
        try {
            URL url = new URL(hostJson);
            URLConnection urlConnection = url.openConnection();
            urlConnection.addRequestProperty("User-Agent", "Mozilla");
            InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());
            hostData = gson.fromJson(reader, HostData.class);
            reader.close();
            log.info(hostData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hostData;
    }

    @Override
    public String toString() {
        return "HostData{" +
                "OSRS_Hosts=" + Arrays.toString(OSRS_Hosts) +
                '}';
    }

    public static class Host {
        String Username;
        int World;
        String loc;

        public Host(String user, int w, String l){
            Username = user;
            World = w;
            loc = l;
        }

        @Override
        public String toString() {
            return "Host{" +
                    "Username='" + Username + '\'' +
                    ", World=" + World +
                    ", loc='" + loc + '\'' +
                    '}';
        }
    }

}
