package com.andre601.helpgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.ConfigKey;
import com.andre601.helpgui.util.players.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CmdHelp extends BaseCommand {

    private HelpGUI plugin;

    public CmdHelp(HelpGUI plugin){
        this.plugin = plugin;
    }

    @CommandAlias("help")
    @Description("Lists all online players or searches for certain one(s)")
    @CommandCompletion("group:|uuid:")
    @Syntax("/help [name|group:<group>|uuid:<uuid>]")
    public void onDefault(Player player, @Optional PlayerUtil search){
        if(player.hasPermission("helpgui.disablehelp")){
            plugin.getFormatUtil().sendMessage(player, plugin.getConfig().getString(
                    ConfigKey.ERR_CMD_DISABLED.getPath()
            ));
            return;
        }
        if(plugin.getConfig().getString(ConfigKey.DW_MODE.getPath()).equalsIgnoreCase("whitelist")){
            if(!plugin.getConfig().getStringList(
                    ConfigKey.DISABLED_WORLDS.getPath()).contains(player.getWorld().getName()
            )){
                plugin.getFormatUtil().sendMessage(player, plugin.getConfig().getString(
                        ConfigKey.ERR_DISABLED_WORLD.getPath()
                ));
                return;
            }
        }else{
            if(plugin.getConfig().getStringList(
                    ConfigKey.DISABLED_WORLDS.getPath()).contains(player.getWorld().getName()
            )){
                plugin.getFormatUtil().sendMessage(player, plugin.getConfig().getString(
                        ConfigKey.ERR_DISABLED_WORLD.getPath()
                ));
                return;
            }
        }

        if(search == null) return;

        ArrayList<ItemStack> items = search.getPlayers();

        if(!search.isSearchSuccess()) return;

        if(items.size() == 0){
            plugin.getFormatUtil().sendMessage(player, plugin.getConfig().getString(
                    ConfigKey.ERR_NO_PLAYERS_FOUND.getPath()
            ));
            return;
        }

        plugin.setScrollerInventory(items, plugin.getFormatUtil().formatText(
                plugin.getConfig().getString(ConfigKey.INV_TITLE.getPath())
        ), player);
    }

}
