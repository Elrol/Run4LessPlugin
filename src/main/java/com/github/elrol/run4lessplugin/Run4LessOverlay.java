package com.github.elrol.run4lessplugin;

import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Run4LessOverlay extends Overlay {

    private final PanelComponent panelComponent = new PanelComponent();

    private ImageComponent logoComp;

    public Run4LessOverlay(){
        setPriority(OverlayPriority.MED);
        setPosition(OverlayPosition.TOP_LEFT);
    }

    public void setLogo(BufferedImage logo){
        logoComp = new ImageComponent(logo);
        update();
    }

    private void update() {
        List<LayoutableRenderableEntity> children = panelComponent.getChildren();
        children.clear();
        children.add(logoComp);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        return panelComponent.render(graphics);
    }

}
