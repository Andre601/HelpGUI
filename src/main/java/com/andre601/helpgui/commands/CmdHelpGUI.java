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
            plugin.getFormatUtil().sendMsg(player, ConfigKey.ERR_NO_PERMISSION);
            return;
        }

        this.plugin.getLogUtil().info("Reloading config.yml...");
        plugin.getFormatUtil().sendMsg(player, ConfigKey.MSG_CONFIG_ATTEMPREL);
        this.plugin.reloadConfig();
        this.plugin.getLogUtil().info("Reload complete!");
        plugin.getFormatUtil().sendMsg(player, ConfigKey.MSG_CONFIG_RELOADED);

        Bukkit.getOnlinePlayers().stream()
                .filter(pl -> pl.hasPermission(""))
                .filter(pl -> pl != player)
                .forEach(pl -> plugin.getFormatUtil().sendMsg(
                        pl,
                        ConfigKey.MSG_CONFIG_REL_NOTIFY_PLAYER,
                        "%player%",
                        pl.getName()
                ));
    }

    @Default
    @HelpCommand
    @Description("Shows this help page.")
    public void onHelp(Player player, CommandHelp help){
        if(!player.hasPermission("helpgui.help")){
            plugin.getFormatUtil().sendMsg(player, ConfigKey.ERR_NO_PERMISSION);
            return;
        }
        help.showHelp();
    }
}
