package com.github.elrol.run4lessplugin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class BoneDashClientPanel extends JPanel {
    public JTextField clientName;
    public JComboBox<RunType> runType;
    public JTextField pricePerInv;
    public JTextField pricePerRun;
    public JTextField bonesToRun;
    public JButton runButton;
    public JButton spoiler;

    private boolean isHidden = false;
    public BoneDashClientPanel() {
        RunnerStats.RunnerData current = Run4LessPlugin.stats.currentRun;

        clientName = new JTextField("");
        clientName.setHorizontalAlignment(JTextField.CENTER);
        pricePerInv = new JTextField("");
        pricePerInv.setHorizontalAlignment(JTextField.CENTER);
        pricePerRun = new JTextField("");
        pricePerRun.setHorizontalAlignment(JTextField.CENTER);
        bonesToRun = new JTextField("");
        bonesToRun.setHorizontalAlignment(JTextField.CENTER);

        pricePerRun.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                if(!pricePerInv.getText().isEmpty()) pricePerInv.setText("");
                //updateRunInfo(true);
            }
            public void removeUpdate(DocumentEvent e) {}
            public void changedUpdate(DocumentEvent e) {}
        });

        pricePerInv.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                if(!pricePerRun.getText().isEmpty()) pricePerRun.setText("");
                //updateRunInfo(false);
            }
            public void removeUpdate(DocumentEvent e) {}
            public void changedUpdate(DocumentEvent e) {}
        });

        runType = new JComboBox<>();
        runType.addItem(RunType.none);
        runType.addItem(RunType.afk);
        runType.addItem(RunType.tick);

        runType.addActionListener(e -> {
            RunType type = (RunType) runType.getSelectedItem();
            switch (Objects.requireNonNull(type)) {
                case afk:
                    pricePerInv.setText("25k");
                    pricePerInv.setForeground(Color.GREEN);
                    break;
                case tick:
                    pricePerInv.setText("15k");
                    pricePerInv.setForeground(Color.GREEN);
                    break;
                default:
                    pricePerInv.setForeground(Color.WHITE);
                    break;
            }
        });

        runButton = new JButton();

        runButton.addActionListener(e -> {
            String text = runButton.getText();
            RunnerStats stats = Run4LessPlugin.stats;
            if(text.equalsIgnoreCase("start run")) {
                int qty = parseText(bonesToRun.getText());

                int price;
                if(pricePerInv.getText().isEmpty()){
                    float trips = (float) Math.ceil((double)qty / 26.0d);
                    price = Math.round((float)parseText(pricePerRun.getText()) / trips);
                } else {
                    price = parseText(pricePerInv.getText());
                }

                String name = clientName.getText();
                String typeName = String.valueOf(runType.getSelectedItem());

                if(!name.isEmpty() && price > 0 && qty > 0 && !typeName.equalsIgnoreCase("none")) {
                    stats.startRun(clientName.getText(), price, RunType.valueOf(typeName));
                    updateButton();
                }
            } else {
                stats.endRun();
                updateButton();
            }
        });
        updateButton();

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = c.weighty = 1.0f;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        JLabel header = new JLabel("Run Manager", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 16));
        header.setBorder(new EmptyBorder(10,10,10,10));
        panel.add(header, c);

        c.gridy++;
        c.gridwidth = 1;
        panel.add(new JLabel("Client:"), c);

        c.gridx++;
        panel.add(clientName, c);

        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Run Type:"), c);

        c.gridx++;
        panel.add(runType, c);

        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Bones per Run"), c);

        c.gridx++;
        panel.add(bonesToRun, c);

        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Cost per Inv"), c);

        c.gridx++;
        panel.add(pricePerInv, c);

        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel("Cost per Run"), c);

        c.gridx++;
        panel.add(pricePerRun, c);

        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 2;
        panel.add(runButton, c);

        spoiler = new JButton("Hide Run Manager");
        spoiler.addActionListener(e -> {
            if(isHidden) {
                panel.setVisible(true);
                isHidden = false;
                spoiler.setText("Hide Run Manager");
            } else {
                panel.setVisible(false);
                isHidden = true;
                spoiler.setText("Show Run Manager");
            }
        });

        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add(spoiler);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(p1);
        add(new JSeparator());
        add(panel);
    }

    private void updateButton() {
        RunnerStats stats = Run4LessPlugin.stats;
        if(stats.currentRun == null) {
            runButton.setText("Start Run");
        } else {
            runButton.setText("End Run");
            clientName.setText(stats.currentRun.client);
            pricePerInv.setText(String.valueOf(stats.currentRun.price));
        }
    }

    private int parseText(String string){
        String s = string.replace(" ", "").toLowerCase();

        float mult = 1;
        if(s.contains("k")) {
            s = s.replace("k","");
            mult = 1000;
        } else if(s.contains("m")) {
            s = s.replace("m","");
            mult = 1000000;
        } else if(s.contains("b")) {
            s = s.replace("b","");
            mult = 1000000000;
        }
        return Math.round(Float.parseFloat(s) * mult);
    }
}
