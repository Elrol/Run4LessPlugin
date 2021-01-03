package com.github.elrol.run4lessplugin;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatManager;
import net.runelite.api.FriendsChatRank;
import net.runelite.api.Player;
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

    @Inject
    private Client client;

    private ArrayList<ChatMessage> clanMessages = new ArrayList<>();
    private PanelComponent panelComponent = new PanelComponent();

    public Run4LessCCOverlay(){
        setPriority(OverlayPriority.HIGHEST);
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
    }

    public void init(int width, ChatMessage message){
        clanMessages.add(message);
        init(width);
    }

    public void init(int width){
        Player s = client.getLocalPlayer();
        if(s == null) return;
        panelComponent.setPreferredSize(new Dimension(width, 0));
        panelComponent.getChildren().clear();
        int last = clanMessages.size()-1;
        ArrayList<ChatMessage> msgs = new ArrayList<>();
        for(int i = last; i >= 0; i--){
            if(msgs.size() >= config.ccLines()) break;
            ChatMessage msg = clanMessages.get(i);
            if(config.clientFilterEnabled()) {
                if (!config.clientName().isEmpty() && (config.clientName().equalsIgnoreCase(msg.getName()) || msg.getName().equalsIgnoreCase(s.getName()))) {
                    msgs.add(msg);
                }
            } else {
                msgs.add(msg);
            }
        }
        for(int i = msgs.size()-1; i >= 0; i--) {
            panelComponent.getChildren().add(colorMessage(msgs.get(i), s.getName()));
        }
    }

    private LineComponent colorMessage(ChatMessage msg, String name){
        Color color = config.ccColor();
        if(client.getFriendsChatManager() != null){
            FriendsChatRank rank = client.getFriendsChatManager().findByName(msg.getName()).getRank();
            if(rank != null){
                if(rank != FriendsChatRank.UNRANKED) color = config.ccRColor();
                if(rank.equals(FriendsChatRank.FRIEND)) color = config.ccHostColor();
            }
            if(msg.getMessageNode().getName().equalsIgnoreCase(name))
                color = config.ccSColor();
        }
        if(msg.getName().equalsIgnoreCase(config.clientName())) color = config.ccClientColor();
        return LineComponent.builder()
                .leftColor(color)
                .left(msg.getMessageNode().getName() + ": " + msg.getMessage())
                .build();
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        return panelComponent.render(graphics);
    }
}
