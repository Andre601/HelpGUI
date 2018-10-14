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
                player.sendMessage(
                        ConfigKey.PREFIX.getString(true) +
                        ConfigKey.ERR_VAULT_NOT_ENABLED.getString(true)
                );
                return null;
            }

            if(search.substring(6).equals("")){
                player.sendMessage(
                        ConfigKey.PREFIX.getString(true) +
                        ConfigKey.ERR_NO_GROUP.getString(true));
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
        switch (ConfigKey.DP_MODE.getString(false).toUpperCase()){
            case "WHITELIST":
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(player != requester) {
                        if(ConfigKey.DISABLED_PLAYERS.getStringList(false).contains(player.getName())){
                            players.add(getPlayerhead(player));
                        }
                    }
                }
                break;

            case "STAFF":
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(player != requester) {
                        if(player.hasPermission("helpgui.staff")){
                            players.add(getPlayerhead(player));
                        }
                    }
                }
                break;

            case "BLACKLIST":
            default:
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(player != requester){
                        players.add(getPlayerhead(player));
                    }
                }
                break;
        }
        return players;
    }

    private List<ItemStack> searchByName(Player requester, String name){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getName().startsWith(name)){
                if(player != requester) {
                    players.add(getPlayerhead(player));
                }
            }
        }
        return players;
    }

    private List<ItemStack> searchByGroup(Player requester, String group){
        for(Player player : Bukkit.getOnlinePlayers()){
            if(this.plugin.getVaultIntegrationManager().getPrimaryGroup(player).equalsIgnoreCase(group)){
                if(player != requester) {
                    players.add(getPlayerhead(player));
                }
            }
        }

        return players;
    }



    private ItemStack getPlayerhead(Player player){
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta)playerHead.getItemMeta();
        headMeta.setOwningPlayer(player);
        headMeta.setDisplayName(player.getName());
        headMeta.setLore(ConfigKey.MSG_PLAYER_LORE.getStringList(player));
        playerHead.setItemMeta(headMeta);

        return playerHead;

    }
}
