package com.andre601.helpgui.util.config;

import com.andre601.helpgui.HelpGUI;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public enum Messages {

    // Prefix of the plugin
    PREFIX("Messages.HelpInv.Title"),

    // Main settings
    SHOW_BANNER("Main.ShowBanner"),
    DP_MODE("Main.DisabledPlayers.Mode"),
    DISABLED_PLAYERS("Main.DisabledPlayers.Players"),
    DW_MODE("Main.DisabledWorlds.Mode"),
    DISABLED_WORLDS("Main.DisabledWorlds.Worlds"),

    // Inv/Item text
    MSG_INV_INFO("Messages.HelpInv.Info"),
    MSG_INV_INFO_DESC("Messages.HelpInv.InfoDesc"),
    MSG_NEXT_PAGE("Messages.HelpInv.NextPage"),
    MSG_PREV_PAGE("Messages.HelpInv.PrevPage"),
    MSG_PLAYER_LORE("Messages.HelpInv.PlayerLore"),

    // Startup messages
    MSG_VAULT_FOUND("Messages.Startup.VaultFound"),
    MSG_VAULT_NOT_FOUND("Messages.Startup.VaultNotFound"),
    MSG_PAPI_FOUND("Messages.Startup.PlaceholderAPIFound"),
    MSG_PAPI_NOT_FOUND("Messages.Startup.PlaceholderAPINotFound"),

    // Help request
    MSG_HELP_SEND("Messages.HelpRequest.Send"),
    MSG_HELP_RECEIVED("Messages.HelpRequest.Receiver"),

    // Errors
    MSG_CONFIG_ATTEMPREL("Messages.Config.AttemptReaload"),
    MSG_CONFIG_RELOADED("Messages.Config.Reloaded"),
    MSG_CONFIG_REL_NOTIFY_PLAYER("Messages.Config.ReloadNotifyPlayer"),
    ERR_CMD_DISABLED("Messages.Errors.CommandDisabled"),
    ERR_NOT_ONLINE("Messages.Errors.NotOnline"),
    ERR_DISABLED_WORLD("Messages.Errors.DisabledWorld"),
    ERR_NO_PLAYERS_FOUND("Messages.Errors.NoPlayersFound"),
    ERR_NO_GROUP("Messages.Errors.NoGroup"),
    ERR_VAULT_NOT_ENABLED("Messages.Errors.VaultNotEnabled"),
    ERR_NO_PERMISSION("Messages.Errors.NoPermission");

    private final String PATH;

    Messages(String path){
        this.PATH = path;
    }

    public String getString(boolean colored){
        if(colored)
            return ChatColor.translateAlternateColorCodes('&', HelpGUI.getInstance().getConfig().getString(this.PATH));

        return HelpGUI.getInstance().getConfig().getString(this.PATH);
    }

    public List<String> getStringList(boolean colored){
        if(!colored)
            return HelpGUI.getInstance().getConfig().getStringList(this.PATH);

        List<String> color = new ArrayList<>();
        for(String str : HelpGUI.getInstance().getConfig().getStringList(this.PATH)){
            color.add(ChatColor.translateAlternateColorCodes('&', str));
        }
        return color;
    }

    public List<String> getStringList(Player player){
        List<String> colored = new ArrayList<>();
        for(String str : HelpGUI.getInstance().getConfig().getStringList(this.PATH)){
            if(HelpGUI.getPlaceholderAPIStatus())
                colored.add(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(
                        player, str
                )));
            else
                colored.add(ChatColor.translateAlternateColorCodes('&', str));
        }
        return colored;
    }

    public boolean getBoolean(){
        return HelpGUI.getInstance().getConfig().getBoolean(this.PATH);
    }
}