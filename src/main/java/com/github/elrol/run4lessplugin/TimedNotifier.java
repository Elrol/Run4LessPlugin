package com.github.elrol.run4lessplugin;

import net.runelite.client.ui.overlay.OverlayManager;

import java.util.Timer;
import java.util.TimerTask;

public class TimedNotifier {
    private static OverlayManager manager;
    private static RunnerNotificationOverlay overlay;
    public static Timer timer;
    public static int time;
    public static String text;

    public static void init(String txt, int seconds, OverlayManager m){
        manager = m;
        text = txt;
        time = seconds;

        if(overlay == null) {
            overlay = new RunnerNotificationOverlay();
            overlay.init(text, (time & 1) == 0);
            manager.add(overlay);
        } else {
            overlay.flash((time & 1) == 0);
        }

        timer = new Timer();
        timer.schedule(new NotifierTask(), 1000);
    }

    private static class NotifierTask extends TimerTask {

        @Override
        public void run() {
            if(time > 0) {
                time--;
                init(text, time, manager);
            } else {
                manager.remove(overlay);
                overlay = null;
            }
        }
    }



}
