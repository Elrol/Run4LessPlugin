package com.github.elrol.run4lessplugin;

import com.google.gson.annotations.SerializedName;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.annotations.EverythingIsNonNull;

import java.io.*;
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
        if(hostJson == null || hostJson.isEmpty()) return;
        //OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(hostJson).build();
        Run4LessPlugin.INSTANCE.httpClient.newCall(req).enqueue(new Callback() {
            @Override
            @EverythingIsNonNull
            public void onFailure(Call call, IOException e) {}

            @Override
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                InputStreamReader reader = new InputStreamReader(response.body().byteStream(), StandardCharsets.UTF_8);
                HostData data = Run4LessPlugin.INSTANCE.gson.fromJson(reader, HostData.class);
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
