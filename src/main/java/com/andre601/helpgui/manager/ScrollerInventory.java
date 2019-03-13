package com.andre601.helpgui.manager;

import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.ConfigKey;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Public class to create a multi-page Inventory system.
 * <br>
 * <br>Original author: https://www.spigotmc.org/threads/infinite-inventory-with-pages.178964/
 */
public class ScrollerInventory {

    private List<Inventory> pages = new ArrayList<>();
    private int currentPage = 0;
    private Map<UUID, ScrollerInventory> users = new HashMap<>();
    private HelpGUI plugin;

    List<Inventory> getPages(){
        return pages;
    }

    int getCurrentPage() {
        return currentPage;
    }

    Map<UUID, ScrollerInventory> getUsers() {
        return users;
    }

    public ScrollerInventory(HelpGUI plugin, ArrayList<ItemStack> items, String name, Player player){
        this.plugin = plugin;

        Inventory page = getBlankPage(player, name);
        for(int i = 0; i < items.size(); i++){
            // Checks, if first empty slot is at pos 54
            if(page.firstEmpty() == 53){
                page.addItem(items.get(i));
                pages.add(page);
                /*
                * Had to add this weird "fix" because the plugin created a blank GUI, if the last item fills the last
                * slot of the other inv...
                */
                if(i < (items.size() - 1))
                    page = getBlankPage(player, name);
            }else{
                page.addItem(items.get(i));
            }
        }
        pages.add(page);
        player.openInventory(pages.get(currentPage));
        users.put(player.getUniqueId(), this);
    }

    // Function to create new empty page.
    private Inventory getBlankPage(Player player, String name){

        Inventory page = Bukkit.createInventory(null, 54, name);
        ItemMeta meta;

        /*
         * Item for next page
         */
        ItemStack nextPage = new ItemStack(plugin.getFormatUtil().getItem(ConfigKey.INV_ITEM_NEXT_PAGE_MATERIAL));
        meta = nextPage.getItemMeta();
        meta.setDisplayName(plugin.getFormatUtil().formatText(player, ConfigKey.INV_ITEM_NEXT_PAGE_NAME));
        meta.setLore(plugin.getFormatUtil().formatLore(player, ConfigKey.INV_ITEM_NEXT_PAGE_LORE));
        nextPage.setItemMeta(meta);

        /*
         * Item for previous page
         */
        ItemStack prevPage = new ItemStack(plugin.getFormatUtil().getItem(ConfigKey.INV_ITEM_PREV_PAGE_MATERIAL));
        meta = prevPage.getItemMeta();
        meta.setDisplayName(plugin.getFormatUtil().formatText(player, ConfigKey.INV_ITEM_PREV_PAGE_NAME));
        meta.setLore(plugin.getFormatUtil().formatLore(player, ConfigKey.INV_ITEM_PREV_PAGE_LORE));
        prevPage.setItemMeta(meta);

        /*
         * Item for filling empty slots
         */
        ItemStack deco = new ItemStack(plugin.getFormatUtil().getItem(ConfigKey.INV_ITEM_FILLER_MATERIAL));
        meta = deco.getItemMeta();
        meta.setDisplayName(plugin.getFormatUtil().formatText(player, ConfigKey.INV_ITEM_FILLER_NAME));
        meta.setLore(plugin.getFormatUtil().formatLore(player, ConfigKey.INV_ITEM_FILLER_LORE));
        deco.setItemMeta(meta);

        /*
         * Item for the info
         */
        ItemStack info = new ItemStack(plugin.getFormatUtil().getItem(ConfigKey.INV_ITEM_INFO_MATERIAL));
        meta = info.getItemMeta();
        meta.setDisplayName(plugin.getFormatUtil().formatText(player, ConfigKey.INV_ITEM_INFO_NAME));
        meta.setLore(plugin.getFormatUtil().formatLore(player, ConfigKey.INV_ITEM_INFO_LORE));
        info.setItemMeta(meta);

        page.setItem(0, prevPage);
        page.setItem(1, deco);
        page.setItem(2, deco);
        page.setItem(3, deco);
        page.setItem(4, info);
        page.setItem(5, deco);
        page.setItem(6, deco);
        page.setItem(7, deco);
        page.setItem(8, nextPage);

        return page;
    }

}
