package com.github.elrol.run4lessplugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class RunnerStats {

    private static final File dataLoc = new File("./bonerunning_logs/");
    Map<String, RunnerData> runHistory = new HashMap<>();
    Map<String, Integer> bonesRan = new HashMap<>();
    int totalMade;

    public RunnerStats(){
        totalMade = 0;
    }

    public void addRun(String client, String bones, int count, int coins, int notes, String noted){
        RunnerData data = runHistory.getOrDefault(client, new RunnerData(client));
        if(!bones.isEmpty() && count > 0) {
            int qty = data.items.getOrDefault(bones, 0);
            data.items.put(bones, qty + count);
            qty = bonesRan.getOrDefault(client, 0);
            bonesRan.put(client, qty + count);
        }
        if(coins > 0){
            totalMade += coins;
            data.price += coins;
        }
        if(notes > 0 && !noted.isEmpty()){
            int notedQty = data.notes.getOrDefault(noted, 0);
            data.notes.put(noted, notedQty + notes);
        }
        runHistory.put(client, data);
        //System.out.println("Client: " + client + ", Bones: " + bones + ", Count: " + count + ", Coins: " + coins + ", Notes: " + notes + ", Noted: " + noted);
        save();
    }

    public void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if(!dataLoc.exists()) dataLoc.mkdirs();
        //System.out.println("DataLoc: " + dataLoc.getAbsoluteFile());
        try(FileWriter writer = new FileWriter(new File(dataLoc, "rundata.json"))) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RunnerStats load() {
        Gson gson = new Gson();
        RunnerStats stats;
        try {
            stats = gson.fromJson(new FileReader(new File(dataLoc, "rundata.json")), RunnerStats.class);
        } catch (FileNotFoundException e) {
            stats = new RunnerStats();
        }
        return stats;
    }

    public int totalBones(){
        int total = 0;
        for(int i : bonesRan.values()){
            total += i;
        }
        return total;
    }

    public static class RunnerData {
        public String client;
        public Map<String, Integer> items = new HashMap<>();
        public Map<String, Integer> notes = new HashMap<>();
        public int price;

        public RunnerData(String client){
            this.client = client;
            price = 0;
        }
    }

}
