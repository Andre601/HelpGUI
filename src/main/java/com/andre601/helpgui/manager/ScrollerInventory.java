package com.andre601.helpgui.manager;

import com.andre601.helpgui.util.config.Messages;
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
     * This code was not made by me.
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
            if(page.firstEmpty() == 53){
                page.addItem(items.get(i));
                pages.add(page);
                /*
                * Had to add this weird "fix" because the plugin created a blank GUI, if the last item fills the last
                * slot of the other inv...
                */
                if(i < (items.size() - 1))
                    page = getBlankPage(name);
            }else{
                page.addItem(items.get(i));
            }
        }
        pages.add(page);
        p.openInventory(pages.get(currentPage));
        users.put(p.getUniqueId(), this);
    }

    // Displayname for NextPage-Item
    public static final String NEXT_PAGE_NAME = Messages.MSG_NEXT_PAGE.getString(true);

    // Displayname for PrevPage-Item
    public static final String PREV_PAGE_NAME = Messages.MSG_PREV_PAGE.getString(true);

    // Displayname for the Info-Item
    public static final String INFO = Messages.MSG_INV_INFO.getString(true);

    public static final String DECO = ChatColor.GRAY.toString();

    // Function to create new empty page.
    private Inventory getBlankPage(String name){

        Inventory page = Bukkit.createInventory(null, 54, name);

        ItemStack nextPage = new ItemStack(Material.SIGN);
        ItemMeta meta = nextPage.getItemMeta();
        meta.setDisplayName(NEXT_PAGE_NAME);
        nextPage.setItemMeta(meta);

        ItemStack prevPage = new ItemStack(Material.SIGN);
        meta = prevPage.getItemMeta();
        meta.setDisplayName(PREV_PAGE_NAME);
        prevPage.setItemMeta(meta);

        ItemStack deco = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        meta = deco.getItemMeta();
        meta.setDisplayName(DECO);
        deco.setItemMeta(meta);

        ItemStack info = new ItemStack(Material.BOOK);
        meta = info.getItemMeta();
        meta.setDisplayName(INFO);
        meta.setLore(Messages.MSG_INV_INFO_DESC.getStringList(true));
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
