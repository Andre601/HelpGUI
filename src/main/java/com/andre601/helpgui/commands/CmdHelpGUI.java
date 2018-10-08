package com.andre601.helpgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.Messages;
import com.andre601.helpgui.util.logging.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("helpgui|hgui")
public class CmdHelpGUI extends BaseCommand {

    @Subcommand("reload")
    @Description("Reloads the config.yml")
    public void reloadConfig(Player player){
        if(!player.hasPermission("helpgui.reload")){
            player.sendMessage(
                    Messages.PREFIX.getString(true) + Messages.ERR_NO_PERMISSION.getString(true)
            );
            return;
        }

        LogUtil.LOG("&7Reloading config.yml...");
        player.sendMessage(Messages.PREFIX.getString(true) + Messages.MSG_CONFIG_ATTEMPREL.getString(true));
        HelpGUI.getInstance().reloadConfig();
        LogUtil.LOG("&7Reload complete!");
        player.sendMessage(Messages.PREFIX.getString(true) + Messages.MSG_CONFIG_RELOADED.getString(true));

        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.hasPermission("helpgui.notify"))
                if(p != player)
                    p.sendMessage(Messages.PREFIX.getString(true) +
                            Messages.MSG_CONFIG_REL_NOTIFY_PLAYER.getString(true)
                                    .replace("%player%", player.getName())
                    );
        }
    }

    @Default
    @HelpCommand
    @Description("Shows this help page.")
    public void onHelp(Player player, CommandHelp help){
        if(!player.hasPermission("helpgui.help")){
            player.sendMessage(Messages.PREFIX.getString(true) + Messages.ERR_NO_PERMISSION.getString(true));
            return;
        }
        help.showHelp();
    }
}
