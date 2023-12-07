package com.github.elrol.run4lessplugin;

import java.sql.Date;

public class Host {

    String Username;
    int World;
    String loc;

    Date start;

    String key;

    public Host(String key, String user, int world, String location, Date start){
        this.key = key;
        this.Username = user;
        this.World = world;
        this.loc = location;
        this.start = start;
    }

    @Override
    public String toString() {
        return "Host{" +
                "Key='" + key + '\'' +
                ", Username='" + Username + '\'' +
                ", World=" + World +
                ", loc='" + loc + '\'' +
                ", start='" + start + '\'' +
                '}';
    }
}
