package com.github.elrol.run4lessplugin;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RunnerStats {

    private static final File dataLoc = new File(RuneLite.RUNELITE_DIR, "/bonerunning_logs/");
    Map<String, RunnerData> runHistory = new HashMap<>();
    Map<String, Integer> bonesRan = new HashMap<>();
    int totalMade;
    RunData current;

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
            if(current != null) current.ran(bones, count);
        }
        if(coins > 0){
            totalMade += coins;
            data.price += coins;
        }
        if(notes > 0 && !noted.isEmpty()){
            int notedQty = data.notes.getOrDefault(noted, 0);
            data.notes.put(noted, notedQty + notes);
            if (current == null) {
                current = new RunData(client, noted, notes);
                updateRun(client);
            } else {
                if (current.client.equalsIgnoreCase(client)) {
                    current.add(noted, notes);
                    updateRun(client);
                } else {
                    updateRun(client);
                    current = new RunData(client, noted, notes);
                }
            }
        }
        runHistory.put(client, data);
        log.debug("Client: " + client + ", Bones: " + bones + ", Count: " + count + ", Coins: " + coins + ", Notes: " + notes + ", Noted: " + noted);
        save();
    }

    public void updateRun(String client){
        log.info("updating run");
        if(current == null) return;
        if(!current.client.equalsIgnoreCase(client) || !current.check()){
            double now = System.currentTimeMillis();
            log.info(String.valueOf(current.start));
            log.info(String.valueOf(now));
            log.info(String.valueOf(now - (double)current.start));
            long seconds = Math.round((double)(System.currentTimeMillis() - current.start) / 60000D);
            RunnerData data = runHistory.getOrDefault(client, new RunnerData(client));
            data.totalTime += seconds;
            runHistory.put(client, data);
            //TODO Database info send
            //Send all the data to the server here
            log.info("time for run was: " + seconds + " seconds");
        }
        if(!current.check()) current = null;
        save();
    }

    public void save() {
        if(!dataLoc.exists()) dataLoc.mkdirs();
        log.debug("DataLoc: " + dataLoc.getAbsoluteFile());
        try(FileWriter writer = new FileWriter(new File(dataLoc, "rundata.json"))) {
            Run4LessPlugin.INSTANCE.gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RunnerStats load() {
        RunnerStats stats;
        try {
            stats = Run4LessPlugin.INSTANCE.gson.fromJson(new FileReader(new File(dataLoc, "rundata.json")), RunnerStats.class);
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
        public long totalTime;

        public RunnerData(String client){
            this.client = client;
            price = 0;
            totalTime = 0L;
        }
    }

}
