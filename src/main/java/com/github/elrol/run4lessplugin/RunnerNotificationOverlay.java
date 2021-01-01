package com.github.elrol.run4lessplugin;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;

import java.awt.*;

public class RunnerNotificationOverlay extends Overlay {

    private final PanelComponent panelComponent = new PanelComponent();
    private String text;

    public void init(String text, boolean state){
        this.text = text;
        setPriority(OverlayPriority.HIGHEST);
        setPosition(OverlayPosition.TOP_CENTER);

        //panelComponent.setPreferredSize(new Dimension(150,0));
        panelComponent.setWrap(false);
        panelComponent.getChildren().add(LineComponent.builder().left(text).leftColor(state ? Color.white : Color.red).build());
    }

    public void flash(boolean state){
        panelComponent.getChildren().clear();
        panelComponent.getChildren().add(LineComponent.builder().left(text).leftColor(state ? Color.white : Color.red).preferredSize(new Dimension()).build());
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        return panelComponent.render(graphics);
    }
}
