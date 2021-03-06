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
    private boolean searchSuccess;

    private HelpGUI plugin;

    public PlayerUtil(HelpGUI plugin){
        this.plugin = plugin;
    }

    public void search(Player player, String search){
        players.clear();
        if(search == null){
            searchAll(player);
            searchSuccess = true;
        }else
        if(search.startsWith("group:")){
            if(!this.plugin.isVaultEnabled()){
                plugin.getFormatUtil().sendMsg(player, ConfigKey.ERR_VAULT_NOT_ENABLED);
                searchSuccess = false;
                return;
            }

            if(search.substring(6).isEmpty()){
                plugin.getFormatUtil().sendMsg(player, ConfigKey.ERR_NO_GROUP);

                plugin.getLogUtil().debug("Didn't find any players through Search method \"group:\"");
                plugin.getLogUtil().debug("Reason: No group was provided!");

                searchSuccess = false;
                return;
            }
            searchByGroup(player, search.substring(6));
            searchSuccess = true;
        }else
        if(search.startsWith("uuid:")) {
            if(search.substring(5).equals("")){
                plugin.getFormatUtil().sendMsg(player, ConfigKey.ERR_NO_UUID);

                plugin.getLogUtil().debug("Didn't find any players through Search method \"uuid:\"");
                plugin.getLogUtil().debug("Reason: No UUID was provided!");

                searchSuccess = false;
                return;
            }
            searchByUUID(player, search.substring(5));
            searchSuccess = true;
        }else{
            searchByName(player, search);
            searchSuccess = true;
        }
    }

    public ArrayList<ItemStack> getPlayers(){
        return players;
    }

    public boolean isSearchSuccess() {
        return searchSuccess;
    }

    private List<ItemStack> searchAll(Player requester){
        switch(plugin.getConfig().getString(ConfigKey.DP_MODE.getPath()).toUpperCase()){
            case "WHITELIST":
                Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(player -> isInList(player, plugin.getConfig().getStringList(
                                ConfigKey.DISABLED_PLAYERS.getPath()
                        )))
                        .filter(player -> player != requester)
                        .forEach(player -> players.add(getPlayerhead(player)));
                break;

            case "STAFF":
                Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(player -> player.hasPermission("helpgui.staff"))
                        .filter(player -> player != requester)
                        .forEach(player -> players.add(getPlayerhead(player)));
                break;

            case "BLACKLIST":
            default:
                Bukkit.getOnlinePlayers()
                        .stream()
                        .filter(player -> !isInList(player, plugin.getConfig().getStringList(
                                ConfigKey.DISABLED_PLAYERS.getPath()
                        )))
                        .filter(player -> player != requester)
                        .forEach(player -> players.add(getPlayerhead(player)));
        }
        return players;
    }

    private List<ItemStack> searchByName(Player requester, String name){
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getName().startsWith(name))
                .filter(player -> player != requester)
                .forEach(player -> players.add(getPlayerhead(player)));

        return players;
    }

    private List<ItemStack> searchByUUID(Player requester, String uuid){
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.getUniqueId().toString().equals(uuid))
                .filter(player -> player != requester)
                .forEach(player -> players.add(getPlayerhead(player)));

        return players;
    }

    private List<ItemStack> searchByGroup(Player requester, String group){

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> plugin.getVaultManager().getPrimaryGroup(player).equalsIgnoreCase(group))
                .filter(player -> player != requester)
                .forEach(player -> players.add(getPlayerhead(player)));

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

        String name = plugin.getFormatUtil().formatText(player, ConfigKey.INV_ITEM_PLAYER_NAME);
        List<String> lore = plugin.getFormatUtil().formatLore(player, ConfigKey.INV_ITEM_PLAYER_LORE);

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
