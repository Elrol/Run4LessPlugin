package com.github.elrol.run4lessplugin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.DecimalFormat;

public class BoneCalcPanel extends JPanel {

    private final JTextField boneQty;
    private final JTextField price;
    private final JTextField totalCost;

    private boolean isHidden = true;
    public BoneCalcPanel(){
        JButton reset;
        JButton calc;
        JButton spoiler;

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
            double total = (Math.floor((((float)bones)) / 26.0F) * (float)p);
            DecimalFormat formatter = new DecimalFormat("#,###");
            totalCost.setText(formatter.format(total));
        });

        JPanel calcPanel = new JPanel();
        calcPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        JLabel header = new JLabel("Bone Calculator", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 16));
        header.setBorder(new EmptyBorder(10,10,10,10));
        calcPanel.add(header, c);

        c.gridy++;
        c.gridwidth = 1;
        calcPanel.add(new JLabel("Bone Qty:"), c);

        c.gridx++;
        calcPanel.add(new JLabel("Price per Inv:"), c);

        c.gridx = 0;
        c.gridy++;
        calcPanel.add(boneQty,c);

        c.gridx++;
        calcPanel.add(price, c);

        c.gridx = 0;
        c.gridy++;
        calcPanel.add(reset, c);

        c.gridx++;
        calcPanel.add(calc, c);

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy++;
        calcPanel.add(new JLabel("Price for Run:"), c);

        c.gridy++;
        calcPanel.add(totalCost, c);
        calcPanel.setVisible(false);

        spoiler = new JButton("Show Bone Calculator");
        spoiler.addActionListener(e -> {
            if(isHidden) {
                calcPanel.setVisible(true);
                isHidden = false;
                spoiler.setText("Hide Bone Calculator");
            } else {
                calcPanel.setVisible(false);
                isHidden = true;
                spoiler.setText("Show Bone Calculator");
            }
        });

        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new BorderLayout());
        add(spoiler, BorderLayout.PAGE_START);
        add(new JSeparator());
        add(calcPanel, BorderLayout.PAGE_END);
    }

    private int parseString(String s){
        s = s.replace("b", "000000000");
        s = s.replace("m", "000000");
        s = s.replace("k", "000");
        return Integer.parseInt(s);
    }
}
