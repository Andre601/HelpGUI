package com.andre601.helpgui.manager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EventManager implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void on(InventoryClickEvent e){

        if(!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player)e.getWhoClicked();
        if(!(ScrollerInventory.users.containsKey(p.getUniqueId()))) return;

        ScrollerInventory inv = ScrollerInventory.users.get(p.getUniqueId());
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getItemMeta() == null) return;
        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.nextPageName)){
            e.setCancelled(true);
            if(inv.currentPage >= inv.pages.size()-1){
                return;
            }else{
                inv.currentPage += 1;
                p.openInventory(inv.pages.get(inv.currentPage));
            }
        }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.prevPageName)){
            e.setCancelled(true);
            if(inv.currentPage > 0){
                inv.currentPage -= 1;
                p.openInventory(inv.pages.get(inv.currentPage));
            }
        }

    }

}
