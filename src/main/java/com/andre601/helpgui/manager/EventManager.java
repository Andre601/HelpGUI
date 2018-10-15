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
import org.bukkit.inventory.meta.ItemMeta;

public class EventManager implements Listener {

    private HelpGUI plugin;
    private ScrollerInventory inventory;

    public EventManager(HelpGUI plugin){
        this.plugin = plugin;
        inventory = this.plugin.getScrollerInventory();
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e){

        if(!(e.getWhoClicked() instanceof Player)) return;

        Player p = (Player)e.getWhoClicked();
        if(!inventory.getUsers().containsKey(p.getUniqueId())) return;

        ScrollerInventory inv = inventory.getUsers().get(p.getUniqueId());
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getItemMeta() == null) return;
        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        int currentPage = inv.getCurrentPage();
        if(e.getCurrentItem().getItemMeta().getDisplayName().equals(
                this.plugin.getScrollerInventory().NEXT_PAGE_NAME
        )){
            e.setCancelled(true);
            if(currentPage >= inv.getPages().size()-1){
                return;
            }else{
                currentPage += 1;
                p.openInventory(inv.getPages().get(currentPage));
            }
        }else if(e.getCurrentItem().getItemMeta().getDisplayName().equals(
                this.plugin.getScrollerInventory().PREV_PAGE_NAME
        )){
            e.setCancelled(true);
            if(currentPage > 0){
                currentPage -= 1;
                p.openInventory(inv.getPages().get(currentPage));
            }
        }else if(e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
            e.setCancelled(true);
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            try{
                Player recipient = Bukkit.getServer().getPlayer(ChatColor.stripColor(meta.getDisplayName()));

                recipient.sendMessage(ConfigKey.PREFIX.getString(true) + ConfigKey.MSG_HELP_RECEIVED.getString(true)
                        .replace("%sender%", p.getName()));
                p.sendMessage(ConfigKey.PREFIX.getString(true) + ConfigKey.MSG_HELP_SEND.getString(true)
                        .replace("%recipient%", recipient.getName()));

                p.closeInventory();
                inventory.getUsers().remove(p.getUniqueId());
            }catch (Exception ex){
                p.sendMessage(ConfigKey.PREFIX.getString(true) + ConfigKey.ERR_NOT_ONLINE.getString(true));
            }

        }else{
            e.setCancelled(true);
        }

    }

}
