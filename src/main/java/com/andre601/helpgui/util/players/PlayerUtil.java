package com.andre601.helpgui.util.players;

import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.ConfigKey;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class PlayerUtil {

    private static ArrayList<ItemStack> players = new ArrayList<>();
    private HelpGUI plugin;

    public PlayerUtil(HelpGUI plugin){
        this.plugin = plugin;
    }

    public List<ItemStack> search(Player player, String search){
        players.clear();
        if(search == null){
            return searchAll(player);
        }else
        if(search.startsWith("group:")){
            if(!this.plugin.getVaultStatus()){
                plugin.getFormatUtil().sendMessage(player, plugin.getConfig().getString(
                        ConfigKey.ERR_VAULT_NOT_ENABLED.getKey()
                ));
                return null;
            }

            if(search.substring(6).equals("")){
                plugin.getFormatUtil().sendMessage(player, plugin.getConfig().getString(
                        ConfigKey.ERR_NO_GROUP.getKey()
                ));
                return null;
            }
            return searchByGroup(player, search.substring(6));
        }else{
            return searchByName(player, search);
        }
    }

    public ArrayList<ItemStack> getPlayers(){
        return players;
    }

    private List<ItemStack> searchAll(Player requester){
        switch(plugin.getConfig().getString(ConfigKey.DP_MODE.getKey()).toUpperCase()){
            case "WHITELIST":
                Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(player -> isInList(player, plugin.getConfig().getStringList(
                                ConfigKey.DISABLED_PLAYERS.getKey()
                        )))
                        .filter(player -> player != requester)
                        .forEach(player -> players.add(getPlayerhead(player)));
                break;

            case "STAFF":
                Bukkit.getOnlinePlayers().stream().filter(
                        player -> player.hasPermission("helpgui.staff")
                ).filter(player -> player != requester).forEach(player -> players.add(getPlayerhead(player)));
                break;

            case "BLACKLIST":
            default:
                Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(player -> !isInList(player, plugin.getConfig().getStringList(
                                ConfigKey.DISABLED_PLAYERS.getKey()
                        )))
                        .filter(player -> player != requester)
                        .forEach(player -> players.add(getPlayerhead(player)));
        }
        return players;
    }

    private List<ItemStack> searchByName(Player requester, String name){
        Bukkit.getOnlinePlayers().stream().filter(
                player -> player.getName().startsWith(name)
        ).filter(player -> player != requester).forEach(player -> players.add(getPlayerhead(player)));

        return players;
    }

    private List<ItemStack> searchByGroup(Player requester, String group){

        Bukkit.getOnlinePlayers().stream().filter(
                player -> plugin.getVaultManager().getPrimaryGroup(player).equalsIgnoreCase(group)
        ).filter(player -> player != requester).forEach(player -> players.add(getPlayerhead(player)));

        return players;
    }

    private boolean isInList(Player player, List<String> names){
        for(String name : names){
            if(name.startsWith("uuid:") && player.getUniqueId().toString().equals(name.replace("uuid:", "")))
                return true;
            if(player.getName().equals(name))
                return true;
        }
        return false;
    }

    private ItemStack getPlayerhead(Player player){

        String name = plugin.getFormatUtil().formatText(player, ConfigKey.INV_ITEM_PLAYER_NAME.getKey());
        List<String> lore = plugin.getFormatUtil().formatLore(player, ConfigKey.INV_ITEM_PLAYER_LORE.getKey());

        name = name.replace("%player%", player.getName());

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta)playerHead.getItemMeta();

        headMeta.setOwningPlayer(player);
        headMeta.setDisplayName(name);
        headMeta.setLore(lore);
        
        playerHead.setItemMeta(headMeta);

        return playerHead;

    }
}
