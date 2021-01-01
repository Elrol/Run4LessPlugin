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
                position = 2,
                keyName = "hideRE",
                name = "Hide Random Events [NYI]",
                description = "Will hide the npcs from random events.",
                section = settings
        )
        default boolean hideREEnabled() { return false; }

        @ConfigItem(
                position = 3,
                keyName = "filterTrade",
                name = "Filter Trade Messages",
                description = "Will remove all trade messages, other then trade offers.",
                section = settings
        )
        default boolean filterTradeEnabled() { return false; }

        @ConfigItem(
                position = 4,
                keyName = "splitCC",
                name = "Split Clan Messages",
                description = "Will display clan chat messages above the chat window.",
                section = settings
        )
        default boolean splitCCEnabled() { return false; }

        @ConfigItem(
                position = 5,
                keyName = "ccLines",
                name = "Lines of Clan Chat",
                description = "How many line of clan chat should be displayed.",
                section = settings
        )
        default int ccLines() { return 0; }

        @ConfigItem(
                position = 6,
                keyName = "ccColor",
                name = "Clan Chat Color",
                description = "Default Color for Clan Chat",
                section = settings
        )
        default Color ccColor() { return Color.WHITE; }

        @ConfigItem(
                position = 7,
                keyName = "ccRColor",
                name = "Ranked Clan Chat Color",
                description = "Color for Ranked Clan Members",
                section = settings
        )
        default Color ccRColor() { return Color.GREEN; }

    //////////////////////////
    //--------Client--------//
    //////////////////////////
    @ConfigSection(
            name = "Client Settings [NYI]",
            description = "Settings related to Bone Runner Clients",
            position = 100
    )
    String clientSettings = "clientSettings";


    //////////////////////////
    //--------Runner--------//
    //////////////////////////
    @ConfigSection(
            name = "Runner Settings",
            description = "Settings related to Bone Runners",
            position = 200
    )
    String runnerSettings = "runnerSettings";

        @ConfigItem(
                position = 201,
                keyName = "offerAll",
                name = "Offer All Items",
                description = "Will offer all of the item clicked when trading.",
                section = runnerSettings
        )
        default boolean offerAllEnabled() { return false; }

        @ConfigItem(
                position = 202,
                keyName = "clientFilter",
                name = "Client Filter",
                description = "Filters different things using the Client Name.",
                section = runnerSettings
        )
        default boolean clientFilterEnabled() { return false; }

        @ConfigItem(
                position = 203,
                keyName = "ccClientColor",
                name = "Clan Chat Client Color",
                description = "Will offer all of the item clicked when trading.",
                section = runnerSettings
        )
        default Color ccClientColor() { return Color.YELLOW; }

        @ConfigItem(
                position = 204,
                keyName = "clientName",
                name = "Client's Name",
                description = "Will filter other settings to make the client stand out.",
                section = runnerSettings
        )
        default String clientName() { return ""; }

    @ConfigItem(
            position = 205,
            keyName = "enableStats",
            name = "Runner Stats",
            description = "Logs the stats from trading.",
            section = runnerSettings
    )
    default boolean enableStats() { return true; }
}