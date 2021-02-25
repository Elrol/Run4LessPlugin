package com.github.elrol.run4lessplugin;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

import java.awt.*;

@ConfigGroup("run4less")
public interface Run4LessConfig extends Config {

    //////////////////////////
    //-------Settings-------//
    //////////////////////////
    @ConfigSection(
            name = "General Settings",
            description = "Settings for general features",
            position = 0
    )
    String settings = "settings";

        @ConfigItem(
                position = 1,
                keyName = "autoSS",
                name = "Auto Screenshot Trades [NYI]",
                description = "Automatically will take screenshots of trade windows.",
                section = settings
        )
        default boolean autoSSEnabled() { return false; }

        @ConfigItem(
                position = 3,
                keyName = "filterTrade",
                name = "Filter Trade Messages",
                description = "Will remove all trade messages, other then trade offers.",
                section = settings
        )
        default boolean filterTradeEnabled() { return true; }

        @ConfigItem(
                position = 10,
                keyName = "spamTrade",
                name = "Trade Spam",
                description = "Spams incoming trade requests to you.",
                section = settings
        )
        default boolean spamTrade() { return true; }

    //////////////////////////
    //---------Clan---------//
    //////////////////////////
    @ConfigSection(
            name = "Chat Settings",
            description = "Settings for the clan chat",
            position = 100
    )
    String clanchat = "clanchat";

        @ConfigItem(
                position = 101,
                keyName = "ccName",
                name = "ClanChat Owner",
                description = "The owner of the group you are in.",
                section = clanchat
        )
        default String ccName() { return "Run4Less"; }

        @ConfigItem(
                position = 102,
                keyName = "splitCC",
                name = "Split Clan Messages",
                description = "Will display clan chat messages above the chat window.",
                section = clanchat
        )
        default boolean splitCCEnabled() { return true; }

        @ConfigItem(
                position = 103,
                keyName = "ccLines",
                name = "Lines of Clan Chat",
                description = "How many line of clan chat should be displayed.",
                section = clanchat
        )
        default int ccLines() { return 5; }

        @ConfigItem(
                position = 104,
                keyName = "ccColor",
                name = "Clan Chat Color",
                description = "Default color for clan chat",
                section = clanchat
        )
        default Color ccColor() { return Color.WHITE; }

        @ConfigItem(
                position = 105,
                keyName = "ccRColor",
                name = "Ranked Clan Chat Color",
                description = "Color for ranked members in clan chat.",
                section = clanchat
        )
        default Color ccRColor() { return Color.GREEN; }

        @ConfigItem(
                position = 106,
                keyName = "ccSColor",
                name = "Your Clan Chat Color",
                description = "Color for your own messages in clan chat.",
                section = clanchat
        )
        default Color ccSColor() { return Color.BLUE; }

        @ConfigItem(
                position = 107,
                keyName = "ccClientColor",
                name = "Clan Chat Client Color",
                description = "Color for your clients messages in clan chat.",
                section = clanchat
        )
        default Color ccClientColor() { return Color.YELLOW; }


        @ConfigItem(
                position = 108,
                keyName = "ccHostColor",
                name = "Clan Chat Host Color",
                description = "Color for Hosts in clan chat.",
                section = clanchat
        )
        default Color ccHostColor() { return Color.RED; }

    //////////////////////////
    //--------Runner--------//
    //////////////////////////
    @ConfigSection(
            name = "Runner Settings",
            description = "Settings related to Bone Runners",
            position = 300
    )
    String runnerSettings = "runnerSettings";

        @ConfigItem(
                position = 301,
                keyName = "offerAll",
                name = "Offer All Items",
                description = "Will offer all of the item clicked when trading.",
                section = runnerSettings
        )
        default boolean offerAllEnabled() { return true; }

        @ConfigItem(
                position = 302,
                keyName = "clientFilter",
                name = "Client Filter",
                description = "Filters different things using the Client Name.",
                section = runnerSettings
        )
        default boolean clientFilterEnabled() { return false; }

        @ConfigItem(
                position = 304,
                keyName = "clientName",
                name = "Client's Name",
                description = "Will filter other settings to make the client stand out.",
                section = runnerSettings
        )
        default String clientName() { return ""; }

        @ConfigItem(
                position = 305,
                keyName = "enableStats",
                name = "Runner Stats",
                description = "Logs the stats from trading.",
                section = runnerSettings
        )
        default boolean enableStats() { return true; }

        @ConfigItem(
                position = 306,
                keyName = "enablePing",
                name = "Runner Ping",
                description = "Pings you when a runner is requested.",
                section = runnerSettings
        )
        default boolean enablePing() { return true; }

        @ConfigItem(
                position = 307,
                keyName = "logoScale",
                name = "Logo Scale",
                description = "Will change the size of the Logo. (x/10)",
                section = runnerSettings
        )
        default int logoScale() { return 10; }

    /////////////////////////
    //--------Hosts--------//
    /////////////////////////
    @ConfigSection(
            name = "Host Settings",
            description = "Settings related to hosts",
            position = 400
    )
    String hostSettings = "hostSettings";

        @ConfigItem(
                position = 401,
                keyName = "hostEnabled",
                name = "Host Overlay [NYI]",
                description = "Enables the host overlay.",
                section = hostSettings
        )
        default boolean hostEnabled() { return false; }

        @ConfigItem(
                position = 402,
                keyName = "hostColor",
                name = "Host Color",
                description = "The color of the host text.",
                section = hostSettings
        )
        default Color hostColor() { return Color.CYAN; }

        @ConfigItem(
                position = 403,
                keyName = "hostLimit",
                name = "Host Limit",
                description = "The number of hosts displayed.",
                section = hostSettings
        )
        default int hostLimit() { return 10; }

    /////////////////////////
    //-----Connections-----//
    /////////////////////////
    @ConfigSection(
            name = "Connection Settings",
            description = "Settings related to external connections",
            position = 500
    )
    String connectionSettings = "connectionSettings";

        @ConfigItem(
                position = 501,
                keyName = "logoURL",
                name = "Logo URL",
                description = "The URL of the logo.",
                section = connectionSettings
        )
        default String logoUrl() { return ""; }

        @ConfigItem(
                position = 502,
                keyName = "hostJson",
                name = "Host Json URL",
                description = "The URL of the json file.",
                section = connectionSettings
        )
        default String hostJson() { return ""; }
}