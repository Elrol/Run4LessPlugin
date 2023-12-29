package com.github.elrol.run4lessplugin;

import com.google.common.util.concurrent.AtomicDouble;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BoneDashStatPanel extends JPanel {

    JPanel currentRun = new JPanel();
    JPanel runnerStats = new JPanel();
    JPanel lastRun = new JPanel();

    // Current Run
    JLabel cur_clientName = new JLabel();
    JPanel cur_items = new JPanel();
    JLabel cur_costPerInv = new JLabel();
    JLabel cur_totalCost = new JLabel();
    JLabel cur_totalDuration = new JLabel();
    JButton cur_spoiler;

    // Latest Run
    JLabel last_clientName = new JLabel();
    JPanel last_items = new JPanel();
    JLabel last_costPerInv = new JLabel();
    JLabel last_totalCost = new JLabel();
    JLabel last_totalDuration = new JLabel();
    JButton last_spoiler;

    // Runner Stats
    JLabel rs_totalBones = new JLabel("", SwingConstants.RIGHT);
    JPanel rs_bones = new JPanel();
    JLabel rs_totalIncome = new JLabel("", SwingConstants.RIGHT);
    JLabel rs_averageTick = new JLabel("", SwingConstants.RIGHT);
    JLabel rs_averageAFK = new JLabel("", SwingConstants.RIGHT);
    JButton rs_spoiler;
    boolean currentIsHidden = true;
    boolean lastIsHidden = true;
    boolean statIsHidden = false;

    public BoneDashStatPanel() {
        Border border = BorderFactory.createCompoundBorder(new BevelBorder(BevelBorder.RAISED), new EmptyBorder(10,5,10,5));
        border = BorderFactory.createCompoundBorder(new EmptyBorder(10,0,10,0), border);

        currentRun.setBorder(new EmptyBorder(10,10,10,10));
        currentRun.setLayout(new GridBagLayout());

        JLabel cur_header = new JLabel("Current Run", SwingConstants.CENTER);
        cur_header.setFont(new Font("Serif", Font.BOLD, 16));
        cur_header.setBorder(new EmptyBorder(10,10,10,10));

        addGrid(currentRun, cur_header, 0, 0, 2,1);

        addGrid(currentRun, new JLabel("Client Name:"), 0,1);
        addGrid(currentRun, cur_clientName, 1,1);

        addGrid(currentRun, new JLabel("Bones:"),0,2,2,1);

        cur_items.setLayout(new GridBagLayout());
        cur_items.setBorder(border);
        addGrid(currentRun, cur_items,0,3,2,1);

        addGrid(currentRun, new JLabel("Price per Inv:"),0,4);
        addGrid(currentRun, cur_costPerInv,1,4);

        addGrid(currentRun, new JLabel("Total Income:"),0,5);
        addGrid(currentRun, cur_totalCost,1,5);

        addGrid(currentRun, cur_totalDuration,0,6,2,1);

        addGrid(currentRun, new JSeparator(), 0,7);

        // Last Run

        lastRun.setBorder(new EmptyBorder(10,10,10,10));
        lastRun.setLayout(new GridBagLayout());

        JLabel last_header = new JLabel("Latest Run", SwingConstants.CENTER);
        last_header.setFont(new Font("Serif", Font.BOLD, 16));
        last_header.setBorder(new EmptyBorder(10,10,10,10));

        addGrid(lastRun, last_header, 0, 0, 2,1);

        addGrid(lastRun, new JLabel("Client Name:"), 0,1);
        addGrid(lastRun, last_clientName, 1,1);

        addGrid(lastRun, new JLabel("Bones:"),0,2,2,1);

        last_items.setLayout(new GridBagLayout());
        last_items.setBorder(border);
        addGrid(lastRun, last_items,0,3,2,1);

        addGrid(lastRun, new JLabel("Price per Inv:"),0,4);
        addGrid(lastRun, last_costPerInv,1,4);

        addGrid(lastRun, new JLabel("Total Income:"),0,5);
        addGrid(lastRun, last_totalCost,1,5);

        addGrid(lastRun, last_totalDuration,0,6,2,1);

        addGrid(lastRun, new JSeparator(), 0,7);

        runnerStats.setBorder(new EmptyBorder(10,10,10,10));
        runnerStats.setLayout(new GridBagLayout());

        JLabel rs_header = new JLabel("Runner Stats", SwingConstants.CENTER);
        rs_header.setFont(new Font("Serif", Font.BOLD, 16));
        rs_header.setBorder(new EmptyBorder(10,10,10,10));

        addGrid(runnerStats, rs_header, 0,0, 2,1);

        addGrid(runnerStats, new JLabel("Bones Ran:"),0,1);
        addGrid(runnerStats, rs_totalBones, 1,1);

        rs_bones.setLayout(new GridBagLayout());
        rs_bones.setBorder(border);
        addGrid(runnerStats, rs_bones, 0,2,2,1);

        addGrid(runnerStats, new JLabel("Total Income"), 0,3);
        addGrid(runnerStats, rs_totalIncome, 1,3);

        addGrid(runnerStats, new JLabel("Average Tick:"), 0,4);
        addGrid(runnerStats, rs_averageTick, 1,4);

        addGrid(runnerStats, new JLabel("Average AFK:"),0,5);
        addGrid(runnerStats, rs_averageAFK, 1,5);

        lastRun.setBorder(new EmptyBorder(10,10,10,10));

        cur_spoiler = new JButton("Show Current Run");
        cur_spoiler.addActionListener(e -> {
            if(currentIsHidden) {
                currentRun.setVisible(true);
                currentIsHidden = false;
                cur_spoiler.setText("Hide Current Run");
            } else {
                currentRun.setVisible(false);
                currentIsHidden = true;
                cur_spoiler.setText("Show Current Run");
            }
        });

        last_spoiler = new JButton("Show Latest Run");
        last_spoiler.addActionListener(e -> {
            if(lastIsHidden) {
                lastRun.setVisible(true);
                lastIsHidden = false;
                last_spoiler.setText("Hide Latest Run");
            } else {
                lastRun.setVisible(false);
                lastIsHidden = true;
                last_spoiler.setText("Show Latest Run");
            }
        });

        rs_spoiler = new JButton("Hide Runner Stats");
        rs_spoiler.setBorder(new EmptyBorder(5,5,5,5));
        rs_spoiler.addActionListener(e -> {
            if(statIsHidden) {
                runnerStats.setVisible(true);
                statIsHidden = false;
                rs_spoiler.setText("Hide Runner Stats");
            } else {
                runnerStats.setVisible(false);
                statIsHidden = true;
                rs_spoiler.setText("Show Runner Stats");
            }
        });
        lastRun.setVisible(false);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add(cur_spoiler, BorderLayout.PAGE_START);

        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(last_spoiler, BorderLayout.PAGE_START);

        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout());
        p3.add(rs_spoiler, BorderLayout.PAGE_START);

        add(p1);
        add(currentRun);
        add(p2);
        add(lastRun);
        add(p3);
        add(runnerStats);
        update();
        updateStats();
    }

    private void addGrid(JPanel panel, JComponent component, int x, int y) {
        addGrid(panel, component, x, y, GridBagConstraints.HORIZONTAL);
    }
    private void addGrid(JPanel panel, JComponent component, int x, int y, int fill) {
        addGrid(panel, component, x, y, 1, 1, fill);
    }
    private void addGrid(JPanel panel, JComponent component, int x, int y, int width, int height) {
        addGrid(panel,component,x,y,width,height, GridBagConstraints.HORIZONTAL);
    }
    private void addGrid(JPanel panel, JComponent component, int x, int y, int width, int height, int fill) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.weightx = c.weighty = 1.0f;
        c.gridwidth = width;
        c.gridheight = height;
        c.fill = fill;
        panel.add(component, c);
    }

    public void update() {
        RunnerStats stats = Run4LessPlugin.stats;
        if(stats == null) return;

        RunnerStats.RunnerData cur = stats.currentRun;
        if(cur == null) {
            currentRun.setVisible(false);
            cur_spoiler.setEnabled(false);
            return;
        } else {
            cur_spoiler.setEnabled(true);
        }

        cur_clientName.setText(cur.client);
        cur_costPerInv.setText("<html>" + Run4LessPlugin.longToString(cur.price));
        cur_totalCost.setText("<html>" + Run4LessPlugin.longToString(cur.totalPrice));

        LocalDateTime start = LocalDateTime.parse(cur.startTime);
        long dur = start.until(LocalDateTime.now(), ChronoUnit.SECONDS);
        cur_totalDuration.setText(Run4LessPlugin.formatTime(dur));

        cur_items.removeAll();

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = c.weighty = 1.0;
        for (String bone : cur.items.keySet()) {
            int qty = cur.items.get(bone);
            JLabel boneName = new JLabel(bone.replace(" bones", ""));
            JLabel boneQty = new JLabel("<HTML>" + Run4LessPlugin.longToString(qty));
            boneQty.setHorizontalAlignment(JLabel.RIGHT);

            c.gridx = 0;
            last_items.add(boneName, c);
            c.gridx++;
            last_items.add(boneQty, c);
            c.gridy++;
        }
    }

    public void updateStats() {
        update();
        RunnerStats stats = Run4LessPlugin.stats;
        if(stats == null) return;

        // Latest Run
        RunnerStats.RunnerData lastData = stats.getLastRun();
        if(lastData != null) {
            last_clientName.setText(lastData.client);
            last_costPerInv.setText("<html>" + Run4LessPlugin.longToString(lastData.price));
            last_totalCost.setText("<html>" + Run4LessPlugin.longToString(lastData.totalPrice));
            last_totalDuration.setText(Run4LessPlugin.formatTime(lastData.totalTime));

            last_items.removeAll();

            GridBagConstraints c = new GridBagConstraints();
            c.weightx = c.weighty = 1.0;
            for (String bone : lastData.items.keySet()) {
                int qty = lastData.items.get(bone);
                JLabel boneName = new JLabel(bone.replace(" bones", ""));
                JLabel boneQty = new JLabel("<HTML>" + Run4LessPlugin.longToString(qty));
                boneQty.setHorizontalAlignment(JLabel.RIGHT);

                c.gridx = 0;
                last_items.add(boneName, c);
                c.gridx++;
                last_items.add(boneQty, c);
                c.gridy++;
            }

            last_spoiler.setEnabled(true);
        } else {
            lastRun.setVisible(false);
            last_spoiler.setEnabled(false);
        }

        // Runner Stats

        AtomicDouble at = new AtomicDouble();
        AtomicDouble aa = new AtomicDouble();
        HashMap<String,Integer> boneMap = new HashMap<>();

        stats.runnerHistory.forEach((start,data)->{
            AtomicInteger totalQty = new AtomicInteger();
            data.items.forEach((bone,qty)->{
                boneMap.put(bone, boneMap.getOrDefault(bone,0) + qty);
                totalQty.addAndGet(qty);
            });
            double avg = (double)data.totalTime / (double) totalQty.get();
            if(data.type.equals(RunType.tick)) {
                at.addAndGet(avg);
            } else {
                aa.addAndGet(avg);
            }
        });

        rs_bones.removeAll();

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.gridwidth = c.gridheight = 1;
        for (String bone : boneMap.keySet()) {
            int qty = boneMap.get(bone);
            JLabel boneName = new JLabel(bone.replace(" bones", ""));
            JLabel boneQty = new JLabel("<HTML>" + Run4LessPlugin.longToString(qty));
            boneQty.setHorizontalAlignment(JLabel.RIGHT);

            c.gridx = 0;
            rs_bones.add(boneName, c);
            c.gridx = 1;
            rs_bones.add(boneQty, c);
        }
        rs_totalBones.setText("<html>" + Run4LessPlugin.longToString(stats.getTotalBones()));
        rs_totalIncome.setText("<html>" + Run4LessPlugin.longToString(stats.getTotalMade()));
        DecimalFormat df = new DecimalFormat("#.##");

        double runs = stats.runnerHistory.size();
        rs_averageAFK.setText("<html>" + df.format(aa.get()/runs) + " <font color='lime'>S/Bone</font></html>");
        rs_averageTick.setText("<html>" + df.format(at.get()/runs) + " <font color='lime'>S/Bone</font></html>");
    }

}
