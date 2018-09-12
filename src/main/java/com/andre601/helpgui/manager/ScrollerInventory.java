package com.andre601.helpgui.manager;

import com.andre601.helpgui.util.ConfigUtil;
import com.andre601.helpgui.util.config.ConfigPaths;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ScrollerInventory {

    /*
     * This code isn't from me!
     * It comes from
     *
     * Link:
     *   https://www.spigotmc.org/threads/infinite-inventory-with-pages.178964/
     */

    public List<Inventory> pages = new ArrayList<>();
    public UUID id;
    public int currentPage = 0;
    public static Map<UUID, ScrollerInventory> users = new HashMap<>();


    public ScrollerInventory(ArrayList<ItemStack> items, String name, Player p){
        this.id = UUID.randomUUID();

        Inventory page = getBlankPage(name);
        for(int i = 0; i < items.size(); i++){
            // Checks, if first empty slot is at pos 46
            if(page.firstEmpty() == 46){
                pages.add(page);
                page = getBlankPage(name);
                page.addItem(items.get(i));
            }else{
                page.addItem(items.get(i));
            }
        }
        pages.add(page);
        p.openInventory(pages.get(currentPage));
        users.put(p.getUniqueId(), this);
    }

    // Displayname for NextPage-Item
    public static final String nextPageName = ConfigUtil.color(ConfigUtil.config().getString(ConfigPaths.MSG_NEXT_PAGE));

    // Displayname for PrevPage-Item
    public static final String prevPageName = ConfigUtil.color(ConfigUtil.config().getString(ConfigPaths.MSG_PREV_PAGE));
    public static final String decoration = ChatColor.GRAY.toString();

    // Function to create new empty page.
    private Inventory getBlankPage(String name){

        Inventory page = Bukkit.createInventory(null, 54, name);

        ItemStack nextPage = new ItemStack(Material.SIGN);
        ItemMeta meta = nextPage.getItemMeta();
        meta.setDisplayName(nextPageName);
        nextPage.setItemMeta(meta);

        ItemStack prevPage = new ItemStack(Material.SIGN);
        meta = prevPage.getItemMeta();
        meta.setDisplayName(prevPageName);
        prevPage.setItemMeta(meta);

        ItemStack deco = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1, (byte)15);
        meta = deco.getItemMeta();
        meta.setDisplayName(decoration);
        deco.setItemMeta(meta);

        page.setItem(0, prevPage);
        page.setItem(1, deco);
        page.setItem(2, deco);
        page.setItem(3, deco);
        page.setItem(4, deco);
        page.setItem(5, deco);
        page.setItem(6, deco);
        page.setItem(7, deco);
        page.setItem(8, nextPage);
        return page;
    }

}