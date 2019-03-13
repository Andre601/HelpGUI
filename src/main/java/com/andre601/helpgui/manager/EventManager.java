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
import org.bukkit.inventory.meta.SkullMeta;

public class EventManager implements Listener {

    private HelpGUI plugin;

    public EventManager(HelpGUI plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){

        ScrollerInventory inventory = plugin.getScrollerInventory();

        Player player = (Player)event.getWhoClicked();

        if(inventory == null) return;
        if(!inventory.getUsers().containsKey(player.getUniqueId())) return;

        ScrollerInventory inv = inventory.getUsers().get(player.getUniqueId());
        if(event.getCurrentItem() == null) return;
        if(event.getCurrentItem().getItemMeta() == null) return;
        if(event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        int currentPage = inv.getCurrentPage();
        if(event.getCurrentItem().getItemMeta().getDisplayName().equals(
                plugin.getFormatUtil().formatText(player, ConfigKey.INV_ITEM_NEXT_PAGE_NAME)
        )){
            event.setCancelled(true);
            if(currentPage >= inv.getPages().size()-1){
                return;
            }else{
                currentPage += 1;
                player.openInventory(inv.getPages().get(currentPage));
            }
        }else if(event.getCurrentItem().getItemMeta().getDisplayName().equals(
                plugin.getFormatUtil().formatText(player, ConfigKey.INV_ITEM_PREV_PAGE_NAME)
        )){
            event.setCancelled(true);
            if(currentPage > 0){
                currentPage -= 1;
                player.openInventory(inv.getPages().get(currentPage));
            }
        }else
        if(event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
            event.setCancelled(true);
            SkullMeta meta = (SkullMeta)event.getCurrentItem().getItemMeta();
            Player recipient = Bukkit.getServer().getPlayer(ChatColor.stripColor(meta.getOwningPlayer().getName()));

            if(recipient == null){
                plugin.getFormatUtil().sendMsg(player, ConfigKey.ERR_NOT_ONLINE);
                return;
            }
            try{

                plugin.getFormatUtil().sendMsg(recipient, ConfigKey.MSG_HELP_RECEIVED, "%sender%", player.getName());

                plugin.getFormatUtil().sendMsg(player, ConfigKey.MSG_HELP_SEND, "%recipient%", recipient.getName());

                plugin.getLogUtil().debug(String.format(
                        "Help-request from %s was send to %s.",
                        player.getName(),
                        recipient.getName()
                ));

                inventory.getUsers().remove(player.getUniqueId());
                player.closeInventory();
            }catch (Exception ex){
                inventory.getUsers().remove(player.getUniqueId());
                player.closeInventory();

                plugin.getLogUtil().debug("There was an error with getting the player! Stacktrace below.");
                if(plugin.isDebug()) ex.printStackTrace();

                plugin.getFormatUtil().sendMsg(player, ConfigKey.ERR_UNKNOWN_ERROR);
            }

        }else{
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event){
        Player player = (Player)event.getPlayer();
        ScrollerInventory inventory = plugin.getScrollerInventory();

        if(inventory == null) return;
        if(!inventory.getUsers().containsKey(player.getUniqueId())) return;

        inventory.getUsers().remove(player.getUniqueId());
    }

}
