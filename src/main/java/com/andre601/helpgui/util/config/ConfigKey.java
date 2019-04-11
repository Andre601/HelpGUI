package com.andre601.helpgui.util.config;

import com.andre601.helpgui.HelpGUI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public enum ConfigKey {

    // Main settings
    DEBUG      ("Settings.Debug"),
    SHOW_BANNER("Settings.ShowBanner"),
    BSTATS     ("Settings.BStats"),

    // Disabled players section
    DP_MODE         ("Settings.DisabledPlayers.Mode"),
    DISABLED_PLAYERS("Settings.DisabledPlayers.Players"),

    // Disabled worlds section
    DW_MODE         ("Settings.DisabledWorlds.Mode"),
    DISABLED_WORLDS ("Settings.DisabledWorlds.Worlds"),

    // Inventory title
    INV_TITLE("Settings.Inventory.Title"),

    // Info item
    INV_ITEM_INFO_MATERIAL("Settings.Inventory.Items.Info.Item"),
    INV_ITEM_INFO_NAME    ("Settings.Inventory.Items.Info.Name"),
    INV_ITEM_INFO_LORE    ("Settings.Inventory.Items.Info.Lore"),

    // Next page item
    INV_ITEM_NEXT_PAGE_MATERIAL("Settings.Inventory.Items.NextPage.Item"),
    INV_ITEM_NEXT_PAGE_NAME    ("Settings.Inventory.Items.NextPage.Name"),
    INV_ITEM_NEXT_PAGE_LORE    ("Settings.Inventory.Items.NextPage.Lore"),

    // Previous page item
    INV_ITEM_PREV_PAGE_MATERIAL("Settings.Inventory.Items.PrevPage.Item"),
    INV_ITEM_PREV_PAGE_NAME    ("Settings.Inventory.Items.PrevPage.Name"),
    INV_ITEM_PREV_PAGE_LORE    ("Settings.Inventory.Items.PrevPage.Lore"),

    // Filler item
    INV_ITEM_FILLER_MATERIAL("Settings.Inventory.Items.Filler.Item"),
    INV_ITEM_FILLER_NAME    ("Settings.Inventory.Items.Filler.Name"),
    INV_ITEM_FILLER_LORE    ("Settings.Inventory.Items.Filler.Lore"),

    // Player item
    INV_ITEM_PLAYER_NAME("Settings.Inventory.Items.Player.Name"),
    INV_ITEM_PLAYER_LORE("Settings.Inventory.Items.Player.Lore"),

    HELPREQ_MSG_SENDER   ("Settings.HelpRequest.Message.Sender"),
    HELPREQ_MSG_RECIPIENT("Settings.HelpRequest.Message.Recipient"),

    // Startup messages
    MSG_VAULT_FOUND    ("Messages.Startup.VaultFound"),
    MSG_VAULT_NOT_FOUND("Messages.Startup.VaultNotFound"),
    MSG_PAPI_FOUND     ("Messages.Startup.PlaceholderAPIFound"),
    MSG_PAPI_NOT_FOUND ("Messages.Startup.PlaceholderAPINotFound"),

    // Config
    MSG_CONFIG_ATTEMPREL        ("Messages.Config.AttemptReload"),
    MSG_CONFIG_RELOADED         ("Messages.Config.Reloaded"),
    MSG_CONFIG_REL_NOTIFY_PLAYER("Messages.Config.ReloadNotifyPlayer"),

    // Errors
    ERR_CMD_DISABLED     ("Messages.Errors.CommandDisabled"),
    ERR_NOT_ONLINE       ("Messages.Errors.NotOnline"),
    ERR_DISABLED_WORLD   ("Messages.Errors.DisabledWorld"),
    ERR_NO_PLAYERS_FOUND ("Messages.Errors.NoPlayersFound"),
    ERR_VAULT_NOT_ENABLED("Messages.Errors.VaultNotEnabled"),
    ERR_NO_PERMISSION    ("Messages.Errors.NoPermission"),
    ERR_NO_GROUP         ("Messages.Errors.NoGroup"),
    ERR_NO_UUID          ("Messages.Errors.NoUUID"),
    ERR_UNKNOWN_ERROR    ("Messages.Errors.UnknownError");

    public final String path;
    public static HelpGUI plugin;

    ConfigKey(String path){
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}