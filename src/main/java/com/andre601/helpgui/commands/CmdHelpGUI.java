package com.andre601.helpgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.ConfigPaths;
import com.andre601.helpgui.util.config.ConfigUtil;
import com.andre601.helpgui.util.logging.LogUtil;
import org.bukkit.entity.Player;

@CommandAlias("helpgui|hgui")
public class CmdHelpGUI extends BaseCommand {

    @Subcommand("reload")
    @Description("Reloads the config.yml")
    @CommandPermission("helpgui.reload")
    public void reloadConfig(Player player){
        LogUtil.INFO("&7Reloading config.yml...");
        player.sendMessage(HelpGUI.getInstance().prefix() + ConfigUtil.color(ConfigPaths.MSG_CONFIG_ATTEMPREL));
        HelpGUI.getInstance().reloadConfig();
        LogUtil.INFO("&7Reload complete!");
        player.sendMessage(HelpGUI.getInstance().prefix() + ConfigUtil.color(ConfigPaths.MSG_CONFIG_RELOADED));
    }

    @Default
    @HelpCommand
    @CommandPermission("helpgui.help")
    @Description("Shows this help page.")
    public void onHelp(CommandHelp help){
        help.showHelp();
    }
}
