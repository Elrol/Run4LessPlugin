package com.github.elrol.run4lessplugin;

import com.google.inject.Inject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.*;

public class RunnerNotificationOverlay extends Overlay {

    @Inject
    private Run4LessConfig config;

    private final PanelComponent panelComponent = new PanelComponent();
    private String text = "";

    public void init(String text, boolean state){
        this.text = text;
        setPriority(OverlayPriority.HIGHEST);
        setPosition(OverlayPosition.TOP_CENTER);
        flash(state);
    }

    public void flash(boolean state){
        Dimension dim = new Dimension(scale(300), scale(100));
        panelComponent.setPreferredSize(dim);
        panelComponent.getChildren().clear();
        panelComponent.setWrap(false);
        panelComponent.getChildren().add(TitleComponent.builder().text(text).color(state ? Color.white : Color.red).preferredSize(new Dimension(scale(100), scale(300))).build());
    }

    public void clearText(){ text = ""; }
    public boolean isNew(){ return text.isEmpty(); }
    public int scale(int base) {
        float scale = 1.0f;
        return Math.round(scale * (float)base);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        return panelComponent.render(graphics);
    }
}
