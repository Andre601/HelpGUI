package com.andre601.helpgui.util.players;

import com.andre601.helpgui.HelpGUIMain;
import com.andre601.helpgui.manager.InventoryManager;
import com.andre601.helpgui.manager.VaultIntegrationManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtil {

    private static ArrayList<ItemStack> players = new ArrayList<>();
    private HelpGUIMain plugin;

    public PlayerUtil(HelpGUIMain plugin){
        this.plugin = plugin;
    }

    public static List<ItemStack> search(String search){
        if(search == null){
            return searchAll();
        }else
        if(search.startsWith("group:")){
            return searchByGroup(search.substring(6));
        }else{
            return searchByName(search);
        }
    }

    public static ArrayList<ItemStack> getPlayers(){
        return players;
    }

    private static List<ItemStack> searchAll(){
        for(Player player : Bukkit.getOnlinePlayers()){
            players.add(InventoryManager.getPlayerhead(player));
        }
        return players;
    }

    private static List<ItemStack> searchByName(String name){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getName().startsWith(name)){
                players.add(InventoryManager.getPlayerhead(player));
            }
        }
        return players;
    }

    private static List<ItemStack> searchByGroup(String group){
        if(!HelpGUIMain.getVaultStatus()){
            return null;
        }

        for(Player player : Bukkit.getOnlinePlayers()){
            if(VaultIntegrationManager.getPrimaryGroup(player).equalsIgnoreCase(group)){
                players.add(InventoryManager.getPlayerhead(player));
            }
        }

        return players;
    }

}
