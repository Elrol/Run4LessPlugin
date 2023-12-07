package com.github.elrol.run4lessplugin;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.FriendsChatManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

@Slf4j
public class Run4LessHostOverlay extends Overlay {

    private final PanelComponent panelComponent = new PanelComponent();

    public Run4LessHostOverlay(){
        setPriority(OverlayPriority.LOW);
        setPosition(OverlayPosition.TOP_LEFT);
    }

    public void init(){
        Color hostColor = Run4LessPlugin.INSTANCE.config.hostColor();
        ArrayList<Host> hosts = getHosts();
        panelComponent.getChildren().clear();
        TitleComponent title = TitleComponent.builder().text("Active Hosts").color(hostColor).build();
        panelComponent.getChildren().add(title);
        panelComponent.getChildren().add(TitleComponent.builder().text("───────────").build());
        int i = 0;
        for(Host host : hosts){
            if(i < Run4LessPlugin.INSTANCE.config.hostLimit()) {
                /**LineComponent line = LineComponent.builder()
                        .leftColor(hostColor)
                        .left(host.Username)
                        //.left(host.Username + " : World " + host.World + " : " + host.loc)
                        .build();
                panelComponent.getChildren().add(line);
                 **/
                panelComponent.getChildren().add(TitleComponent.builder().text(host.Username).color(hostColor).build());
                i++;
            }
        }
    }

    private ArrayList<Host> getHosts() {
        ArrayList<Host> hosts = new ArrayList<>();
        try {
            ResultSet rs = Run4LessPlugin.INSTANCE.getQuery("bone_dash_db", "select * from active_hosts");
            ArrayList<Integer> ids = new ArrayList<>();
            while(rs.next()){
                int id = rs.getInt("id");
                String key = rs.getString("active_hosts_key");
                String name = rs.getString("name");
                Date start = rs.getDate("start_time");

                ids.add(id);
                hosts.add(new Host(key, name, 330, "Rimmington", start));
            }
            System.out.println(ids);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hosts;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        return panelComponent.render(graphics);
    }
}
