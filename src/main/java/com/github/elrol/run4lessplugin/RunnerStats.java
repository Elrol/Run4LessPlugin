package com.github.elrol.run4lessplugin;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

import javax.annotation.Nullable;
import java.io.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RunnerStats {

    private static final File dataLoc = new File(RuneLite.RUNELITE_DIR, "/bonerunning_logs/");
    public Map<String, RunnerData> runnerHistory = new HashMap<>();
    RunnerData currentRun;
    String dateLastRun;

    public RunnerStats(){
    }

    public void startRun(String client, int costPerTrip, RunType type) {
        if(currentRun != null) endRun();
        currentRun = new RunnerData(client, LocalDateTime.now(), costPerTrip, type);
    }

    public void addTrip(String client, String bones, int count) {
        if(bones.isEmpty() || client.isEmpty() || count <= 0) return;
        if(currentRun.client.equalsIgnoreCase(client)) {
            currentRun.addTrip(bones, count);
            save();
        }
    }

    public void endRun(){
        currentRun.end();
        runnerHistory.put(currentRun.startTime, currentRun);
        dateLastRun = currentRun.endTime;
        currentRun = null;
        save();
        Run4LessPlugin.panel.statPanel.updateStats();
    }

    public int getTotalBones() {
        AtomicInteger total = new AtomicInteger();
        runnerHistory.forEach((date, data) -> {
            data.items.forEach((item, qty) -> {
                total.addAndGet(qty);
            });
        });

        if(currentRun != null) currentRun.items.forEach((item, qty) -> {
            total.addAndGet(qty);
        });

        return total.get();
    }

    public int getTotalMade() {
        AtomicInteger total = new AtomicInteger();
        runnerHistory.forEach((date, data) -> {
            total.addAndGet(data.totalPrice);
        });

        if(currentRun != null) total.addAndGet(currentRun.totalPrice);

        return total.get();
    }

    @Nullable
    public RunnerData getLastRun() {
        if(runnerHistory.containsKey(dateLastRun)){
            return runnerHistory.get(dateLastRun);
        }
        return null;
    }

    public static String dateToString(LocalDateTime date) {
        return date.toString();
    }


    public void save() {
        if(!dataLoc.exists()) dataLoc.mkdirs();
        log.debug("DataLoc: " + dataLoc.getAbsoluteFile());
        try(FileWriter writer = new FileWriter(new File(dataLoc, "rundata.json"))) {
            if(Run4LessPlugin.INSTANCE == null) return;
            Gson gson = Run4LessPlugin.INSTANCE.gson;
            if(gson != null) {
                String json= gson.toJson(Run4LessPlugin.stats);
                gson.toJson(Run4LessPlugin.stats, RunnerStats.class, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RunnerStats load() {
        RunnerStats stats = new RunnerStats();
        try {
            if(Run4LessPlugin.INSTANCE == null) return stats;
            Gson gson = Run4LessPlugin.INSTANCE.gson;
            if(gson != null) {
                stats = gson.fromJson(new FileReader(new File(dataLoc, "rundata.json")), RunnerStats.class);
                Run4LessPanel panel = Run4LessPlugin.panel;
                if(panel != null && panel.statPanel != null) {
                    panel.statPanel.update();
                }
            }
        } catch (FileNotFoundException ignored) {}
        return stats;
    }

    public static class RunnerData {
        public String client;

        public String startTime;
        public String endTime;
        public Map<String, Integer> items = new HashMap<>();
        public RunType type;
        public int price;
        public int totalPrice = 0;
        public long totalTime;

        public RunnerData(String client, LocalDateTime startTime, int price, RunType type){
            this.client = client;
            this.startTime = dateToString(startTime);
            this.price = price;
            this.type = type;
            totalTime = 0L;
        }

        public void addTrip(String item, int quantity) {
            totalPrice += (price - (quantity * 5));
            int total = items.getOrDefault(item, 0);
            items.put(item, total + quantity);
        }

        public void end(){
            LocalDateTime now = LocalDateTime.now();
            totalTime = LocalDateTime.parse(startTime).until(now, ChronoUnit.SECONDS);
            endTime = dateToString(now);
        }
    }

}
