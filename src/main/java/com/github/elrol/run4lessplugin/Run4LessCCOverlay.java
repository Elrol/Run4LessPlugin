package com.github.elrol.run4lessplugin;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.game.FriendChatManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.*;

import java.awt.*;
import java.util.ArrayList;

@Slf4j
public class Run4LessCCOverlay extends Overlay {

    @Inject
    private Run4LessConfig config;

    private ArrayList<ChatMessage> clanMessages = new ArrayList<>();
    private PanelComponent panelComponent = new PanelComponent();
    private FriendChatManager manager;

    public void setFCManager(FriendChatManager manager){
        this.manager = manager;
    }

    public void init(int lines, int width, ChatMessage message){
        clanMessages.add(message);
        log.debug("Name: " + message.getName());
        init(lines, width);
    }

    public void init(int lines, int width){
        if(clanMessages.size() >= lines){
            int qty = clanMessages.size();
            for(int i = 0; i < qty; i++){
                if(i < (clanMessages.size() - lines)) clanMessages.remove(i);
            }
        }
        setPriority(OverlayPriority.HIGHEST);
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        panelComponent.setPreferredSize(new Dimension(width, 0));
        panelComponent.getChildren().clear();
        clanMessages.forEach(msg -> {
            Color color = config.ccColor();
            if(manager != null){
                FriendsChatRank rank = manager.getRank(msg.getName());
                if(rank != null && rank != FriendsChatRank.UNRANKED){
                    color = config.ccRColor();
                }
            }
            if(msg.getName().equalsIgnoreCase(config.clientName())) color = config.ccClientColor();
            if(config.clientFilterEnabled()){
                if(!config.clientName().isEmpty() && config.clientName().equalsIgnoreCase(msg.getName())){
                    LineComponent line = LineComponent.builder()
                            .leftColor(color)
                            .left(msg.getMessageNode().getName() + ": " + msg.getMessage())
                            .build();
                    panelComponent.getChildren().add(line);
                }
            } else {
                LineComponent line = LineComponent.builder()
                        .leftColor(color)
                        .left(msg.getMessageNode().getName() + ": " + msg.getMessage())
                        .build();
                panelComponent.getChildren().add(line);
            }
        });
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        return panelComponent.render(graphics);
    }
}
