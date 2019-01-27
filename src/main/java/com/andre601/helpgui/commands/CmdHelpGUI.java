package com.andre601.helpgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.ConfigKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("helpgui|hgui")
public class CmdHelpGUI extends BaseCommand {

    private HelpGUI plugin;

    public CmdHelpGUI(HelpGUI plugin){
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @Description("Reloads the config.yml")
    public void reloadConfig(Player player){
        if(!player.hasPermission("helpgui.reload")){
            player.sendMessage(
                    ConfigKey.PREFIX.getString(true) + ConfigKey.ERR_NO_PERMISSION.getString(true)
            );
            return;
        }

        this.plugin.getLogUtil().info("&7Reloading config.yml...");
        player.sendMessage(ConfigKey.PREFIX.getString(true) + ConfigKey.MSG_CONFIG_ATTEMPREL.getString(true));
        this.plugin.reloadConfig();
        this.plugin.getLogUtil().info("&7Reload complete!");
        player.sendMessage(ConfigKey.PREFIX.getString(true) + ConfigKey.MSG_CONFIG_RELOADED.getString(true));

        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.hasPermission("helpgui.notify"))
                if(p != player)
                    p.sendMessage(ConfigKey.PREFIX.getString(true) +
                            ConfigKey.MSG_CONFIG_REL_NOTIFY_PLAYER.getString(true)
                                    .replace("%player%", player.getName())
                    );
        }
    }

    @Default
    @HelpCommand
    @Description("Shows this help page.")
    public void onHelp(Player player, CommandHelp help){
        if(!player.hasPermission("helpgui.help")){
            player.sendMessage(ConfigKey.PREFIX.getString(true) + ConfigKey.ERR_NO_PERMISSION.getString(true));
            return;
        }
        help.showHelp();
    }
}
