package com.github.elrol.run4lessplugin;

import net.runelite.client.ui.overlay.Overlay;
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

    public Run4LessOverlay(){
        setPriority(OverlayPriority.HIGHEST);
        setPosition(OverlayPosition.TOP_LEFT);
        setPreferredSize(new Dimension(20,20));
        panelComponent.setOrientation(ComponentOrientation.HORIZONTAL);
        panelComponent.getChildren().add(new ImageComponent(getLogo(100,100)));
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        return panelComponent.render(graphics);
    }

    private BufferedImage getLogo(int dimX, int dimY){
        BufferedImage dimg = null;
        BufferedImage image = ImageUtil.getResourceStreamFromClass(getClass(), "/R4L.png");
        Image tmp = image.getScaledInstance(dimX, dimY, Image.SCALE_SMOOTH);
        dimg = new BufferedImage(dimX, dimY, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

}
