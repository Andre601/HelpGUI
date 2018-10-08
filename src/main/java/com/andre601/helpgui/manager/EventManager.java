package com.andre601.helpgui.manager;

import com.andre601.helpgui.util.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class EventManager implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e){

        if(!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player)e.getWhoClicked();
        if(!ScrollerInventory.users.containsKey(p.getUniqueId())) return;

        ScrollerInventory inv = ScrollerInventory.users.get(p.getUniqueId());
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getItemMeta() == null) return;
        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.NEXT_PAGE_NAME)){
            e.setCancelled(true);
            if(inv.currentPage >= inv.pages.size()-1){
                return;
            }else{
                inv.currentPage += 1;
                p.openInventory(inv.pages.get(inv.currentPage));
            }
        }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.PREV_PAGE_NAME)){
            e.setCancelled(true);
            if(inv.currentPage > 0){
                inv.currentPage -= 1;
                p.openInventory(inv.pages.get(inv.currentPage));
            }
        }else if(e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
            e.setCancelled(true);
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            try{
                Player recipient = Bukkit.getServer().getPlayer(ChatColor.stripColor(meta.getDisplayName()));

                recipient.sendMessage(Messages.PREFIX.getString(true) + Messages.MSG_HELP_RECEIVED.getString(true)
                        .replace("%sender%", p.getName()));
                p.sendMessage(Messages.PREFIX.getString(true) + Messages.MSG_HELP_SEND.getString(true)
                        .replace("%recipient%", recipient.getName()));

                p.closeInventory();
                ScrollerInventory.users.remove(p.getUniqueId());
            }catch (Exception ex){
                p.sendMessage(Messages.PREFIX.getString(true) + Messages.ERR_NOT_ONLINE.getString(true));
            }

        }else{
            e.setCancelled(true);
        }

    }

}
