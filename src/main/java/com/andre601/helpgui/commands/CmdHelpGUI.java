package com.andre601.helpgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.ConfigPaths;
import com.andre601.helpgui.util.config.ConfigUtil;
import com.andre601.helpgui.util.logging.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("helpgui|hgui")
public class CmdHelpGUI extends BaseCommand {

    @Subcommand("reload")
    @Description("Reloads the config.yml")
    public void reloadConfig(Player player){
        if(!player.hasPermission("helpgui.reload")){
            player.sendMessage(ConfigUtil.color(ConfigPaths.ERR_NO_PERMISSION));
            return;
        }

        LogUtil.LOG("&7Reloading config.yml...");
        player.sendMessage(HelpGUI.getInstance().prefix() + ConfigUtil.color(ConfigPaths.MSG_CONFIG_ATTEMPREL));
        HelpGUI.getInstance().reloadConfig();
        LogUtil.LOG("&7Reload complete!");
        player.sendMessage(HelpGUI.getInstance().prefix() + ConfigUtil.color(ConfigPaths.MSG_CONFIG_RELOADED));

        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.hasPermission("helpgui.notify"))
                if(p != player)
                    p.sendMessage(HelpGUI.getInstance().prefix() +
                            ConfigUtil.color(ConfigPaths.MSG_CONFIG_REL_NOTIFY_PLAYER)
                                    .replace("%player%", player.getName())
                    );
        }
    }

    @Default
    @HelpCommand
    @Description("Shows this help page.")
    public void onHelp(Player player, CommandHelp help){
        if(!player.hasPermission("helpgui.help")){
            player.sendMessage(HelpGUI.getInstance().prefix() + ConfigUtil.color(ConfigPaths.ERR_NO_PERMISSION));
            return;
        }
        help.showHelp();
    }
}
