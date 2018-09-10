package com.andre601.helpgui.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Optional;
import com.andre601.helpgui.manager.ScrollerInventory;
import com.andre601.helpgui.util.ConfigUtil;
import com.andre601.helpgui.util.config.ConfigPaths;
import com.andre601.helpgui.util.players.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class CmdHelp extends BaseCommand {

    @CommandAlias("help")
    public void onDefault(Player player, @Optional PlayerUtil search){
        if(player.hasPermission("helpgui.disablehelp")){
            player.sendMessage(ConfigUtil.color(ConfigPaths.ERR_CMD_DISABLED));
            return;
        }
        ArrayList<ItemStack> items = search.getPlayers();
        new ScrollerInventory(items, ConfigUtil.color(ConfigPaths.MSG_INV_TITLE), player);
    }

}
