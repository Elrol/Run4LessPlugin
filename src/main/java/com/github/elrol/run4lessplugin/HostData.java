package com.github.elrol.run4lessplugin;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
        for(Host host : OSRS_Hosts)
            users.add(host.Username);
        return users;
    }

    public void load(String hostJson){
        Gson gson = new Gson();
        if(hostJson == null || hostJson.isEmpty()) return;
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(hostJson).build();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {}

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                InputStreamReader reader = new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8);
                HostData data = gson.fromJson(reader, HostData.class);
                Run4LessPlugin.hostData.OSRS_Hosts = data.OSRS_Hosts;
                reader.close();
            }
        });
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
