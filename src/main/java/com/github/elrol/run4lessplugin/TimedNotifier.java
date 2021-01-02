package com.github.elrol.run4lessplugin;

import net.runelite.client.ui.overlay.OverlayManager;

import java.util.Timer;
import java.util.TimerTask;

public class TimedNotifier {
    private static OverlayManager manager;
    public static Timer timer;
    public static int time;
    public static String text;

    public static void init(String txt, int seconds, OverlayManager m, RunnerNotificationOverlay overlay){
        manager = m;
        text = txt;
        time = seconds;

        if(overlay.isNew()) {
            overlay.init(text, (time & 1) == 0);
            manager.add(overlay);
        } else {
            overlay.flash((time & 1) == 0);
        }

        timer = new Timer();
        timer.schedule(new NotifierTask(overlay), 1000);
    }

    private static class NotifierTask extends TimerTask {
        RunnerNotificationOverlay overlay;
        public NotifierTask(RunnerNotificationOverlay overlay){
            this.overlay = overlay;
        }

        @Override
        public void run() {
            if(time > 0) {
                time--;
                init(text, time, manager, overlay);
            } else {
                manager.remove(overlay);
                overlay.clearText();
            }
        }
    }



}
