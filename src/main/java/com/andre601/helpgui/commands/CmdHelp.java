package com.andre601.helpgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.andre601.helpgui.manager.ScrollerInventory;
import com.andre601.helpgui.util.config.Messages;
import com.andre601.helpgui.util.players.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CmdHelp extends BaseCommand {

    @CommandAlias("help")
    @Description("Lists all online players or searches for certain one")
    @CommandCompletion("group:")
    @Syntax("/help [name|group:<group>]")
    public void onDefault(Player player, @Optional PlayerUtil search){
        if(player.hasPermission("helpgui.disablehelp")){
            player.sendMessage(Messages.PREFIX.getString(true) + Messages.ERR_CMD_DISABLED.getString(true));
            return;
        }
        if(Messages.DW_MODE.getString(false).equalsIgnoreCase("whitelist")){
            if(!Messages.DISABLED_WORLDS.getStringList(false).contains(player.getWorld().getName())){
                player.sendMessage(Messages.PREFIX.getString(true) + Messages.ERR_DISABLED_WORLD.getString(true));
                return;
            }
        }else{
            if(Messages.DISABLED_WORLDS.getStringList(false).contains(player.getWorld().getName())){
                player.sendMessage(Messages.PREFIX.getString(true) + Messages.ERR_DISABLED_WORLD.getString(true));
                return;
            }
        }

        if(search == null){
            return;
        }

        ArrayList<ItemStack> items = search.getPlayers();

        if(items.size() == 0){
            player.sendMessage(Messages.PREFIX.getString(true) + Messages.ERR_NO_PLAYERS_FOUND.getString(true));
            return;
        }
        new ScrollerInventory(items, Messages.PREFIX.getString(true), player);
    }

}
