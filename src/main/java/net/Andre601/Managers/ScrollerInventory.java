package net.Andre601.Managers;

import net.Andre601.HelpGUIMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ScrollerInventory {

    public List<Inventory> pages = new ArrayList<Inventory>();
    public UUID id;
    public int currentPage = 0;
    public static Map<UUID, ScrollerInventory> users = new HashMap<UUID, ScrollerInventory>();

    public ScrollerInventory(ArrayList<ItemStack> items, String name, Player p){
        this.id = UUID.randomUUID();

        Inventory page = getBlankPage(name);
        for(int i = 0; i < items.size(); i++){
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

    public static final String nextPageName = ChatColor.translateAlternateColorCodes('&', HelpGUIMain.
            getInstance().Config().getString("Messages.HelpInv.NextPage"));
    public static final String prevPageName = ChatColor.translateAlternateColorCodes('&', HelpGUIMain.
            getInstance().Config().getString("Messages.HelpInv.PrevPage"));
    public static final String decoration = ChatColor.GRAY.toString();
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

        ItemStack deco = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
        meta = deco.getItemMeta();
        meta.setDisplayName(decoration);

        page.setItem(0, nextPage);
        page.setItem(1, deco);
        page.setItem(2, deco);
        page.setItem(3, deco);
        page.setItem(4, deco);
        page.setItem(5, deco);
        page.setItem(6, deco);
        page.setItem(7, deco);
        page.setItem(8, prevPage);
        return page;
    }

}
