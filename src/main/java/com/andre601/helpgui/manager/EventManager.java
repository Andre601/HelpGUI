package com.andre601.helpgui.manager;

import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.ConfigKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class EventManager implements Listener {

    private HelpGUI plugin;

    public EventManager(HelpGUI plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        ScrollerInventory inventory = plugin.getScrollerInventory();

        Player player = (Player)e.getWhoClicked();
        if(!inventory.getUsers().containsKey(player.getUniqueId())) return;

        ScrollerInventory inv = inventory.getUsers().get(player.getUniqueId());
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getItemMeta() == null) return;
        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        int currentPage = inv.getCurrentPage();
        if(e.getCurrentItem().getItemMeta().getDisplayName().equals(
                plugin.getFormatUtil().formatText(plugin.getConfig().getString(
                        ConfigKey.INV_ITEM_NEXT_PAGE_NAME.getPath()
                ))
        )){
            e.setCancelled(true);
            if(currentPage >= inv.getPages().size()-1){
                return;
            }else{
                currentPage += 1;
                player.openInventory(inv.getPages().get(currentPage));
            }
        }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(
                plugin.getFormatUtil().formatText(plugin.getConfig().getString(
                        ConfigKey.INV_ITEM_PREV_PAGE_NAME.getPath()
                ))
        )){
            e.setCancelled(true);
            if(currentPage > 0){
                currentPage -= 1;
                player.openInventory(inv.getPages().get(currentPage));
            }
        }else
        if(e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
            e.setCancelled(true);
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            try{
                Player recipient = Bukkit.getServer().getPlayer(ChatColor.stripColor(meta.getDisplayName()));

                plugin.getFormatUtil().sendMessage(recipient, plugin.getConfig().getString(
                        ConfigKey.MSG_HELP_RECEIVED.getPath()
                ).replace("%sender%", player.getName()));

                plugin.getFormatUtil().sendMessage(player, plugin.getConfig().getString(
                        ConfigKey.MSG_HELP_SEND.getPath().replace("%recipient%", recipient.getName())
                ).replace("%recipient%", recipient.getName()));

                plugin.getLogUtil().debug(String.format(
                        "Help-request from %s was send to %s.",
                        player.getName(),
                        recipient.getName()
                ));

                player.closeInventory();
                inventory.getUsers().remove(player.getUniqueId());
            }catch (Exception ex){
                plugin.getLogUtil().debug("There was an error with getting the player! Stacktrace below.");
                if(plugin.isDebug()) ex.printStackTrace();

                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString(ConfigKey.INV_TITLE.getPath()) +
                        plugin.getConfig().getString(ConfigKey.ERR_NOT_ONLINE.getPath())
                ));
            }

        }else{
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Player player = (Player)event.getPlayer();
        ScrollerInventory inventory = plugin.getScrollerInventory();

        if(!inventory.getUsers().containsKey(player.getUniqueId())) return;

        inventory.getUsers().remove(player.getUniqueId());
    }

}
