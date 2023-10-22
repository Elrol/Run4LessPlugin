package com.github.elrol.run4lessplugin;

import com.google.common.collect.ArrayListMultimap;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.menus.MenuManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import okhttp3.*;
import okhttp3.internal.annotations.EverythingIsNonNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@PluginDescriptor(
        name = "Bone Running Plugin",
        description = "A plugin made for Bone Running, commissioned by the Run4Less group and updated for Bone Dash",
        tags = {"bone dash", "menu", "running", "bone"}
)
public class Run4LessPlugin extends Plugin {
    protected static Run4LessPlugin INSTANCE;
    @Inject
    protected Gson gson;

    @Inject
    protected OkHttpClient httpClient;

    @Inject
    private Run4LessConfig config;

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private Run4LessOverlay run4LessOverlay;

    @Inject
    private Run4LessCCOverlay run4LessCCOverlay;

    @Inject
    private RunnerNotificationOverlay notificationOverlay;

    @Inject
    private Run4LessHostOverlay run4LessHostOverlay;

    @Inject
    private ClientToolbar clientToolbar;

    @Inject
    private ClientThread clientThread;

    @Inject
    private Provider<MenuManager> menuManager;

    @Inject
    private ConfigManager configManager;

    public static NavigationButton panel;

    private boolean isRunner = false;
    private boolean isHost = false;

    private final ArrayListMultimap<String, Integer> indexes = ArrayListMultimap.create();
    public static RunnerStats stats = RunnerStats.load();
    public static HostData hostData = new HostData();
    public static final String setClient = "Set as Client";
    boolean shouldSpam = true;
    public ArrayList<String> hosts = new ArrayList<>();
    public ArrayList<String> hosting = new ArrayList<>();
    public static BufferedImage logo;

    @Override
    protected void startUp() throws Exception {
        INSTANCE = this;
        hostData.load(config.hostJson());
        run4LessHostOverlay.init(hosting);
        if(client != null) menuManager.get().addPlayerMenuItem(setClient);
        if(config.splitCCEnabled() && config.ccLines() > 0) overlayManager.add(run4LessCCOverlay);
        logo = ImageUtil.loadImageResource(getClass(), "/OIG.png");
        logo = resize(logo, config.logoScale());
        panel = NavigationButton.builder()
                .tooltip("Bone Calculator")
                .icon(logo)
                .priority(10)
                .panel(new Run4LessPanel())
                .build();
        clientToolbar.addNavigation(panel);
        updateLogo(getClass(), config.logoUrl());
        super.startUp();
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(run4LessOverlay);
        overlayManager.remove(run4LessCCOverlay);
        overlayManager.remove(notificationOverlay);
        overlayManager.remove(run4LessHostOverlay);
        clientToolbar.removeNavigation(panel);
        menuManager.get().removePlayerMenuItem(setClient);
        configManager.setConfiguration("run4less", "clientName", "");
        stats.updateRun("");
        super.shutDown();
    }

    private boolean isRightClan() {
        FriendsChatManager manager = client.getFriendsChatManager();
        if(manager == null)  return false;
        return Text.standardize(manager.getOwner()).equalsIgnoreCase(Text.standardize(config.ccName()));
    }

    @Subscribe(priority = -2)
    public void onChatMessage(ChatMessage message) {
        FriendsChatManager manager = client.getFriendsChatManager();
        boolean ccEnabled = config.splitCCEnabled();
        int ccLines = config.ccLines();

        if (isRightClan() && message.getType().equals(ChatMessageType.FRIENDSCHAT)) {
            if (ccEnabled && ccLines > 0) {
                final Widget chat = client.getWidget(WidgetInfo.CHATBOX_TRANSPARENT_LINES);
                if (chat != null && !chat.isHidden()) {
                    run4LessCCOverlay.init(chat.getWidth(), message);
                }
            }
            log.debug(message.getMessage());
            if (message.getMessage().toLowerCase().contains("!bones ")) {
                log.debug("Ran bones command");
                String cmd = message.getMessage().toLowerCase().split("!bones ")[1];
                String[] temp = cmd.replace("!bones", "").split(" ");
                int rate = 0;
                if (temp[0].equalsIgnoreCase("afk")) rate = 25000;
                else if (temp[0].equalsIgnoreCase("tick")) rate = 15000;
                else client.addChatMessage(message.getType(), message.getName(), "Invalid argument [" + temp[0] + "]. Options are [tick/afk]", message.getSender());

                int qty = Integer.parseInt(temp[1]);
                if (qty <= 0) {
                    client.addChatMessage(message.getType(), message.getName(), "Invalid argument [" + temp[1] + "]. Options must be greater then 0", message.getSender());
                    qty = 0;
                }
                DecimalFormat formatter = new DecimalFormat("#,###");
                String price = formatter.format(Math.round(((float) qty / 26F) * rate));
                client.addChatMessage(message.getType(), message.getName(), temp[0] + "ing " + temp[1] + " bones would be " + price, message.getSender());
                log.debug(temp[0] + "ing " + temp[1] + " bones would be " + price);
            }
            String sender = message.getName();
            if (!sender.isEmpty()) {
                FriendsChatMember p = manager.findByName(sender);
                if (p != null) {
                    FriendsChatRank rank = p.getRank();
                    String msg = message.getMessage();
                    boolean runner = msg.equalsIgnoreCase("@runner");
                    boolean ping = config.enablePing();
                    if (rank != FriendsChatRank.UNRANKED && runner && isRunner && ping)
                        TimedNotifier.init("Bone Runner Requested", 30, overlayManager, notificationOverlay);
                    if(hosts.contains(p.getName()) && message.getMessage().contains("@host")){
                        if(hosting.contains(p.getName())){
                            hosting.remove(p.getName());
                        } else {
                            hosting.add(p.getName());
                        }
                        run4LessHostOverlay.init(hosting);
                    }
                }
            }
        }
        if (message.getMessage().equalsIgnoreCase("accepted trade.") && config.enableStats()) {
            Widget tradingWith = client.getWidget(334, 30);
            if (tradingWith != null) {
                String rsn = tradingWith.getText().replace("Trading with:<br>", "");
                if (rsn.equalsIgnoreCase(config.clientName())) {
                    Widget partnerTrades = client.getWidget(334, 29);
                    Widget offeredTrades = client.getWidget(334, 28);

                    if (partnerTrades != null && offeredTrades != null) {
                        int i = 0;
                        int coins = 0;
                        int notes = 0;
                        int qty = 0;
                        String bones = "";
                        String noted = "";
                        for (Widget w : partnerTrades.getChildren()) {
                            String text = w.getText();
                            if (text.startsWith("Coins")) {
                                if (text.contains("(")) text = text.split("[(]")[1];
                                else text = text.split("<col=ffffff> x <col=ffff00>")[1];
                                text = text.replace(",", "").replace(")", "");
                                coins += Integer.parseInt(text);
                            } else if (text.toLowerCase().contains("bones") && text.contains("<col=ffffff> x <col=ffff00>")) {
                                String[] temp = text.split("<col=ffffff> x <col=ffff00>");
                                noted = temp[0];
                                notes = Integer.parseInt(temp[1]);
                            } else if (text.toLowerCase().contains("bones")) {
                                qty--;
                            }
                        }
                        for (Widget w : offeredTrades.getChildren()) {
                            if (w == null) continue;
                            log.debug("[" + i++ + "]:" + w.getText());
                            String s = w.getText().toLowerCase();
                            if (s.contains("bones") && !s.contains("<col=ffffff> x <col=ffff00>")) {
                                bones = w.getText();
                                qty++;
                            }
                        }
                        log.info("Adding Run");
                        stats.addRun(rsn, bones, qty, coins, notes, noted);
                    }
                }
            }
        }
        if (config.filterTradeEnabled() && message.getType().equals(ChatMessageType.TRADE)) {
            removeMessage(message);
        }
        if (config.spamTrade() && message.getType().equals(ChatMessageType.TRADEREQ) && shouldSpam){
            shouldSpam = false;
            for (int i = 0; i < 7; i++) {
                client.addChatMessage(message.getType(), message.getName(), message.getMessage(), message.getSender());
            }
            shouldSpam = true;
        }
    }

    @Subscribe
    public void onConfigChanged(ConfigChanged event){
        if(event.getGroup().equals("bone dash")){
            if(config.splitCCEnabled()) {
                final Widget chat = client.getWidget(WidgetInfo.CHATBOX_TRANSPARENT_LINES);
                if(chat != null) run4LessCCOverlay.init(chat.getWidth());
                overlayManager.add(run4LessCCOverlay);
            } else {
                overlayManager.remove(run4LessCCOverlay);
            }
            overlayManager.remove(run4LessOverlay);
            updateLogo(getClass(), config.logoUrl());
            stats.updateRun(config.clientName());
        }
    }

    @Subscribe
    public void onFriendsChatChanged(FriendsChatChanged event){
        clientThread.invokeLater(() -> {
            FriendsChatManager manager = client.getFriendsChatManager();
            Player player = client.getLocalPlayer();
            if(player != null && isRightClan()){
                FriendsChatRank rank = manager.findByName(player.getName()).getRank();
                if(rank.equals(FriendsChatRank.FRIEND)){
                    isHost = true;
                    updateLogo(getClass(), config.logoUrl());
                    return;
                } else if(!rank.equals(FriendsChatRank.UNRANKED)) {
                    isRunner = true;
                    updateLogo(getClass(), config.logoUrl());
                    return;
                } else {
                    isRunner = false;
                    isHost = false;
                }
                hosts.clear();
                for(FriendsChatMember member : manager.getMembers()){
                    if (isPlayerHost(member))
                        hosts.add(member.getName());
                    else
                        hosting.remove(member.getName());
                }
                run4LessHostOverlay.init(hosting);
            }
            overlayManager.remove(run4LessOverlay);
        });
    }

    private boolean isPlayerHost(FriendsChatMember member){
        return (member.getRank().equals(FriendsChatRank.FRIEND) && !hosts.contains(member.getName()))
                || (hostData.getUsers().contains(member.getName()) && !hosts.contains(member.getName()));
    }
    @Subscribe
    public void onFriendsChatMemberJoined(FriendsChatMemberJoined event){
        FriendsChatMember member = event.getMember();
        if (isPlayerHost(member)) {
            hosts.add(member.getName());
        }
    }

    @Subscribe
    public void onFriendsChatMemberLeft(FriendsChatMemberLeft event){
        Player player = client.getLocalPlayer();
        if(player != null) {
            if (event.getMember().getName().equals(player.getName())) {
                hosts.clear();
                hosting.clear();
            } else {
                hosting.remove(event.getMember().getName());
            }
            run4LessHostOverlay.init(hosting);
        }
    }

    @Subscribe
    public void onClientTick(final ClientTick clientTick) {
        if (client.getGameState() != GameState.LOGGED_IN || client.isMenuOpen()) return;
        if(!panel.getIcon().equals(logo)) {
            clientToolbar.removeNavigation(panel);
            panel = NavigationButton.builder()
                    .tooltip("Bone Calculator")
                    .icon(logo)
                    .priority(10)
                    .panel(panel.getPanel())
                    .build();
            if (panel == null) log.info("Navigation was null");
            if (clientToolbar == null) log.info("ClientToolbar was null");
            clientToolbar.addNavigation(panel);
        }
        if (config.offerAllEnabled()) {
                final MenuEntry[] menuEntries = client.getMenuEntries();
            int index = 0;
            indexes.clear();
            for (MenuEntry entry : menuEntries) {
                final String option = Text.removeTags(entry.getOption()).toLowerCase();
                indexes.put(option, index++);
            }

            index = 0;
            for (MenuEntry menuEntry : menuEntries) {
                index++;
                final String option = Text.removeTags(menuEntry.getOption()).toLowerCase();
                final String target = Text.removeTags(menuEntry.getTarget()).toLowerCase();

                if (option.equals("offer")) {
                    final int i = index(indexes, menuEntries, index, option, target);
                    final int id = index(indexes, menuEntries, i, "offer-all", target);

                    if (i >= 0 && id >= 0) {
                        final MenuEntry entry = menuEntries[id];
                        menuEntries[id] = menuEntries[i];
                        menuEntries[i] = entry;

                        client.setMenuEntries(menuEntries);

                        indexes.clear();
                        int idx = 0;
                        for (MenuEntry e : menuEntries) {
                            final String o = Text.removeTags(e.getOption()).toLowerCase();
                            indexes.put(o, idx++);
                        }
                    }
                }
            }
        }
    }

    public static int index(final ArrayListMultimap<String, Integer> optionIndexes, final MenuEntry[] entries, final int limit, final String option, final String target) {
        List<Integer> indexes = optionIndexes.get(option);
        for (int i = indexes.size() - 1; i >= 0; --i) {
            final int idx = indexes.get(i);
            MenuEntry entry = entries[idx];
            String entryTarget = Text.removeTags(entry.getTarget()).toLowerCase();
            if (idx <= limit && entryTarget.equals(target))
                return idx;
        }
        return -1;
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event){
        if (event.getMenuOption().equals(setClient)){
            String name = event.getMenuTarget().split(" {2}\\(level-")[0];
            configManager.setConfiguration("run4less", "clientName", Text.removeTags(name));
        }
    }

    private void removeMessage(ChatMessage msg){
        ChatLineBuffer ccInfoBuffer = client.getChatLineMap().get(ChatMessageType.TRADE.getType());
        if (ccInfoBuffer != null) {
                ccInfoBuffer.removeMessageNode(msg.getMessageNode());
        }
    }

    @Provides
    Run4LessConfig getConfig(ConfigManager configManager){
        return configManager.getConfig(Run4LessConfig.class);
    }

    private static BufferedImage resize(BufferedImage img, int s){
        float scale = (float)s / 10.0f;
        int size = Math.round(60.0f * scale);
        Image tmp = img.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return image;
    }

    private void update(BufferedImage logo){
        Run4LessPanel.init(logo);
        logo = resize(logo, config.logoScale());
        run4LessOverlay.setLogo(logo);
        if ((isRunner || isHost) && !config.logoUrl().equalsIgnoreCase("none") ) overlayManager.add(run4LessOverlay);
    }

    private void updateLogo(Class<?> c, String url){
        logo = ImageUtil.loadImageResource(c, "/OIG.png");
        overlayManager.remove(run4LessOverlay);
        if(!url.equals("") && !url.equalsIgnoreCase("none")) {
            //OkHttpClient client = new OkHttpClient();

            Request req = new Request.Builder().url(url).build();
            httpClient.newCall(req).enqueue(new Callback() {
                @Override
                @EverythingIsNonNull
                public void onFailure(Call call, IOException e) {
                    update(logo);
                    e.printStackTrace();
                }

                @Override
                @EverythingIsNonNull
                public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        synchronized (ImageIO.class) {
                            assert responseBody != null;
                            BufferedImage temp = ImageIO.read(responseBody.byteStream());
                            if(temp != null)
                                logo = resize(temp, config.logoScale());
                            update(logo);
                        }
                    }
                }
            });
        } else {
            update(logo);
        }
    }
}
