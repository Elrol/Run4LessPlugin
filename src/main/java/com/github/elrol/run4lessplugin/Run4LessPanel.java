package com.github.elrol.run4lessplugin;

import net.runelite.client.ui.PluginPanel;

import javax.swing.border.EmptyBorder;

public class Run4LessPanel extends PluginPanel {
    //Run price calculator

    public Run4LessPanel(){
        super();
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(new BoneCalcPanel());
    }


}
