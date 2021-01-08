package com.github.elrol.run4lessplugin;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.PluginPanel;

import javax.swing.border.EmptyBorder;
import java.awt.image.BufferedImage;

@Slf4j
public class Run4LessPanel extends PluginPanel {
    public static BufferedImage logo;

    public static void init(BufferedImage image){
        logo = image;
    }

    public Run4LessPanel(){
        super();
        setBorder(new EmptyBorder(10, 10, 10, 10));
        add(new BoneCalcPanel());
    }


}
