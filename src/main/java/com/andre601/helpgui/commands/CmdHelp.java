package com.andre601.helpgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.manager.ScrollerInventory;
import com.andre601.helpgui.util.config.ConfigPaths;
import com.andre601.helpgui.util.players.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

import static com.andre601.helpgui.util.config.ConfigUtil.color;
import static com.andre601.helpgui.util.config.ConfigUtil.config;

public class CmdHelp extends BaseCommand {

    @CommandAlias("help")
    @Description("Lists all online players or searches for certain one")
    @CommandCompletion("group:")
    @Syntax("/help [name|group:<group>]")
    public void onDefault(Player player, @Optional PlayerUtil search){
        if(player.hasPermission("helpgui.disablehelp")){
            player.sendMessage(HelpGUI.getInstance().prefix() + color(ConfigPaths.ERR_CMD_DISABLED));
            return;
        }
        if(config().getString(ConfigPaths.DW_MODE).equalsIgnoreCase("whitelist")){
            if(!config().getStringList(ConfigPaths.DISABLED_WORLDS).contains(player.getWorld().getName())){
                player.sendMessage(HelpGUI.getInstance().prefix() + color(ConfigPaths.ERR_DISABLED_WORLD));
                return;
            }
        }else{
            if(config().getStringList(ConfigPaths.DISABLED_WORLDS).contains(player.getWorld().getName())){
                player.sendMessage(HelpGUI.getInstance().prefix() + color(ConfigPaths.ERR_DISABLED_WORLD));
                return;
            }
        }

        if(search == null){
            return;
        }

        ArrayList<ItemStack> items = search.getPlayers();

        if(items.size() == 0){
            player.sendMessage(HelpGUI.getInstance().prefix() + color(ConfigPaths.ERR_NO_PLAYERS_FOUND));
            return;
        }
        new ScrollerInventory(items, color(ConfigPaths.MSG_INV_TITLE), player);
    }

}
