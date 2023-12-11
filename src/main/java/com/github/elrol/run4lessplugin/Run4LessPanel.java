package com.github.elrol.run4lessplugin;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.PluginPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;

@Slf4j
public class Run4LessPanel extends PluginPanel {
    public static BufferedImage logo;

    public BoneCalcPanel calcPanel;
    public BoneDashClientPanel clientPanel;
    public BoneDashStatPanel statPanel;

    public static void init(BufferedImage image){
        logo = image;
    }

    public Run4LessPanel(){
        super();

        setBorder(new EmptyBorder(10, 10, 10, 10));

        clientPanel = new BoneDashClientPanel();
        clientPanel.setBorder(new EmptyBorder(10,10,10,10));

        calcPanel = new BoneCalcPanel();
        calcPanel.setBorder(new EmptyBorder(10,10,10,10));

        statPanel = new BoneDashStatPanel();
        statPanel.setBorder(new EmptyBorder(10,10,10,10));

        add(calcPanel);
        add(new JSeparator());
        add(clientPanel);
        add(new JSeparator());
        add(statPanel);
    }


}
