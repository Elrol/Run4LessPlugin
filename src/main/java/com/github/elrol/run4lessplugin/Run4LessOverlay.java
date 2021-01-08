package com.github.elrol.run4lessplugin;

import com.google.inject.Inject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.ImageComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.util.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Run4LessOverlay extends Overlay {

    private final PanelComponent panelComponent = new PanelComponent();
    public String url = "";

    public Run4LessOverlay(){
        setPriority(OverlayPriority.HIGHEST);
        setPosition(OverlayPosition.TOP_LEFT);
        panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);
    }

    public void setLogo(BufferedImage logo){
        panelComponent.getChildren().clear();
        panelComponent.getChildren().add(new ImageComponent(logo));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        return panelComponent.render(graphics);
    }

}
