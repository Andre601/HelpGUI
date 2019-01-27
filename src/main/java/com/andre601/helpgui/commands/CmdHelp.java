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
    @Description("Lists all online players or searches for certain one")
    @CommandCompletion("group:")
    @Syntax("/help [name|group:<group>]")
    public void onDefault(Player player, @Optional PlayerUtil search){
        if(player.hasPermission("helpgui.disablehelp")){
            player.sendMessage(ConfigKey.PREFIX.getString(true) + ConfigKey.ERR_CMD_DISABLED.getString(true));
            return;
        }
        if(ConfigKey.DW_MODE.getString(false).equalsIgnoreCase("whitelist")){
            if(!ConfigKey.DISABLED_WORLDS.getStringList(false).contains(player.getWorld().getName())){
                player.sendMessage(ConfigKey.PREFIX.getString(true) + ConfigKey.ERR_DISABLED_WORLD.getString(true));
                return;
            }
        }else{
            if(ConfigKey.DISABLED_WORLDS.getStringList(false).contains(player.getWorld().getName())){
                player.sendMessage(ConfigKey.PREFIX.getString(true) + ConfigKey.ERR_DISABLED_WORLD.getString(true));
                return;
            }
        }

        if(search == null){
            return;
        }

        ArrayList<ItemStack> items = search.getPlayers();

        if(items.size() == 0){
            player.sendMessage(ConfigKey.PREFIX.getString(true) + ConfigKey.ERR_NO_PLAYERS_FOUND.getString(true));
            return;
        }

        plugin.setScrollerInventory(items, ConfigKey.PREFIX.getString(true), player);
    }

}
