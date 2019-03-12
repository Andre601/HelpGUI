package com.andre601.helpgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.ConfigKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("helpgui|hgui")
public class CmdHelpGUI extends BaseCommand {

    private HelpGUI plugin;

    public CmdHelpGUI(HelpGUI plugin){
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @Description("Reloads the config.yml")
    public void reloadConfig(CommandSender sender){
        if(sender instanceof Player){
            Player player = (Player)sender;

            if(!player.hasPermission("helpgui.reload")){
                plugin.getFormatUtil().sendMsg(player, ConfigKey.ERR_NO_PERMISSION);
                return;
            }

            plugin.getLogUtil().debug("Reloading config.yml...");
            plugin.getFormatUtil().sendMsg(player, ConfigKey.MSG_CONFIG_ATTEMPREL);

            if(reloadConfig()){
                plugin.getLogUtil().debug("Reload successful!");
                plugin.getFormatUtil().sendMsg(player, ConfigKey.MSG_CONFIG_RELOADED);

                Bukkit.getOnlinePlayers().stream()
                        .filter(pl -> pl.hasPermission("helpgui.notify"))
                        .filter(pl -> pl != player)
                        .forEach(pl -> plugin.getFormatUtil().sendMsg(
                                pl,
                                ConfigKey.MSG_CONFIG_REL_NOTIFY_PLAYER,
                                "%player%",
                                pl.getName()
                        ));
            }else
                plugin.getFormatUtil().sendMsg(player, ConfigKey.ERR_UNKNOWN_ERROR);

        }else{
            sender.sendMessage(ChatColor.stripColor(plugin.getConfig().getString(
                    ConfigKey.MSG_CONFIG_ATTEMPREL.getPath()
            )));

            if(reloadConfig()){
                sender.sendMessage(ChatColor.stripColor(plugin.getConfig().getString(
                        ConfigKey.MSG_CONFIG_RELOADED.getPath()
                )));

                Bukkit.getOnlinePlayers().stream()
                        .filter(pl -> pl.hasPermission("helpgui.notify"))
                        .forEach(pl -> plugin.getFormatUtil().sendMsg(
                                pl,
                                ConfigKey.MSG_CONFIG_REL_NOTIFY_PLAYER,
                                "%player%",
                                "CONSOLE"
                        ));
            }
        }
    }

    @Default
    @HelpCommand
    @Description("Shows this help page.")
    public void onHelp(CommandSender sender, CommandHelp help){
        if(sender instanceof Player){
            Player player = (Player)sender;
            if(!player.hasPermission("helpgui.help")){
                plugin.getFormatUtil().sendMsg(player, ConfigKey.ERR_NO_PERMISSION);
                return;
            }
        }
        help.showHelp();
    }

    private boolean reloadConfig(){
        try{
            plugin.reloadConfig();
            return true;
        }catch(Exception ex){
            plugin.getLogUtil().debug("Error while reloading the config! Stacktrace below.");
            if(plugin.isDebug()) ex.printStackTrace();

            return false;
        }
    }
}
