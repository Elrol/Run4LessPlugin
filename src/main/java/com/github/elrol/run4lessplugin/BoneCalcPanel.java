package com.github.elrol.run4lessplugin;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class BoneCalcPanel extends JPanel {

    private final JTextField boneQty;
    private final JTextField price;
    private final JTextField totalCost;

    public BoneCalcPanel(){
        JButton reset;
        JButton calc;

        boneQty = new JTextField("0");
        price = new JTextField("0");
        totalCost = new JTextField("0");
        totalCost.setEditable(false);
        reset = new JButton("Reset");
        reset.addActionListener(e -> {
            boneQty.setText("0");
            price.setText("0");
            totalCost.setText("0");
        });
        calc = new JButton("Calculate");
        calc.addActionListener(e -> {
            int bones = parseString(boneQty.getText());
            boneQty.setText("" + bones);
            int p = parseString(price.getText());
            price.setText("" + p);
            int total = Math.round((((float)bones) / 26.0F) * (float)p);
            DecimalFormat formatter = new DecimalFormat("#,###");
            totalCost.setText(formatter.format(total));
        });
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Bone Qty:"), c);
        c.gridx++;
        add(new JLabel("Price per Inv:"), c);
        c.gridx = 0;
        c.gridy++;
        add(boneQty,c);
        c.gridx++;
        add(price, c);
        c.gridx = 0;
        c.gridy++;
        add(reset, c);
        c.gridx++;
        add(calc, c);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy++;
        add(new JLabel("Price for Run:"), c);
        c.gridy++;
        add(totalCost, c);
    }

    private int parseString(String s){
        s = s.replace("b", "000000000");
        s = s.replace("m", "000000");
        s = s.replace("k", "000");
        return Integer.parseInt(s);
    }
}
