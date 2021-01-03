package com.github.elrol.run4lessplugin;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatManager;
import net.runelite.client.game.FriendChatManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.ComponentOrientation;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.*;
import java.util.ArrayList;

@Slf4j
public class Run4LessHostOverlay extends Overlay {

    @Inject
    private Run4LessConfig config;

    @Inject
    private Client client;

    private PanelComponent panelComponent = new PanelComponent();

    public Run4LessHostOverlay(){
        setPriority(OverlayPriority.HIGHEST);
        setPosition(OverlayPosition.TOP_LEFT);
        panelComponent.setPreferredSize(new Dimension(200, 0));
    }

    public void init(ArrayList<String> list){
        FriendsChatManager manager = client.getFriendsChatManager();
        if(manager == null){
            panelComponent.getChildren().clear();
            return;
        }
        panelComponent.getChildren().clear();
        panelComponent.getChildren().add(TitleComponent.builder().text("Active Hosts").color(config.hostColor()).build());
        int i = 0;
        for(String username : list){
            if(i < config.hostLimit()) {
                int world = manager.findByName(username).getWorld();
                LineComponent line = LineComponent.builder()
                        .leftColor(config.hostColor())
                        .left(username + " : World " + world)
                        .build();
                panelComponent.getChildren().add(line);
                i++;
            }
        }
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        return panelComponent.render(graphics);
    }
}
