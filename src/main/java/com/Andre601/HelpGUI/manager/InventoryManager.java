package com.andre601.helpgui.manager;

import com.andre601.helpgui.HelpGUIMain;
import com.andre601.helpgui.util.config.ConfigPaths;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

import static com.andre601.helpgui.util.ConfigUtil.*;

public class InventoryManager {

    List<ItemStack> item = new ArrayList<>();

    // The two inventories, that will be used.
    private Inventory HelpInv = null;
    private Inventory SearchInv = null;
    private HelpGUIMain main;

    public static ItemStack getPlayerhead(Player player){
        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta)playerHead.getItemMeta();
        headMeta.setOwningPlayer(player);
        playerHead.setItemMeta(headMeta);

        return playerHead;

    }

    // HelpInv for /help
    public void CreateHelpInv(Player p){

        // Creating the actual HelpInv
        HelpInv = p.getPlayer().getServer().createInventory(null, 45, ConfigPaths.MSG_INV_TITLE);

        // Creating the deco-Item (Black stained Glasspane)
        ItemStack Deco = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta DecoMeta = Deco.getItemMeta();
        // Use just a colorcode, to make the itemname empty
        DecoMeta.setDisplayName("§8");
        Deco.setItemMeta(DecoMeta);

        //Creating the Info-Item (Book)
        ItemStack Info = new ItemStack(Material.BOOK);
        ItemMeta InfoMeta = Info.getItemMeta();
        InfoMeta.setDisplayName(color(ConfigPaths.MSG_INV_INFO));
        InfoMeta.setLore(getStringList(color(ConfigPaths.MSG_INV_INFO_DESC)));
        Info.setItemMeta(InfoMeta);

        HelpInv.setItem(4, Info);

        for(Player pl : Bukkit.getOnlinePlayers()){
            if(pl != p){
                /*
                *  Blacklist = only show players, that are NOT in "DisabledPlayer"
                 */
                if(config().getString("Main.Mode").equalsIgnoreCase("blacklist")){
                    if(config().getStringList("Main.DisabledPlayer").contains(pl.getName())){
                        // Make the playerhead and add it to the Inv.
                        ItemStack PlayerHead = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta HeadMeta = (SkullMeta)PlayerHead.getItemMeta();
                        HeadMeta.setOwningPlayer(pl.getPlayer());
                        HeadMeta.setDisplayName(pl.getName());
                        PlayerHead.setItemMeta(HeadMeta);

                        item.add(PlayerHead);
                    }
                }else
                /*
                *  Whitelist = Only show players, that ARE in "DisabledPlayers
                 */
                if(config().getString("Main.Mode").equalsIgnoreCase("whitelist")){
                    if(!config().getStringList("Main.DisabledPlayer").contains(pl.getName())){

                        // Make the playerhead and add it to the Inv.
                        ItemStack PlayerHead = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta HeadMeta = (SkullMeta)PlayerHead.getItemMeta();
                        HeadMeta.setOwningPlayer(pl.getPlayer());
                        HeadMeta.setDisplayName(pl.getName());
                        PlayerHead.setItemMeta(HeadMeta);

                        item.add(PlayerHead);
                    }
                }else
                /*
                *  Staff = Only show players, that have the permission helpgui.staff
                 */
                if(config().getString("Main.Mode").equalsIgnoreCase("staff")){
                    if(pl.hasPermission("helpgui.staff")){

                        // Make the playerhead and add it to the Inv.
                        ItemStack PlayerHead = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta HeadMeta = (SkullMeta)PlayerHead.getItemMeta();
                        HeadMeta.setOwningPlayer(pl.getPlayer());
                        HeadMeta.setDisplayName(pl.getName());
                        PlayerHead.setItemMeta(HeadMeta);

                        item.add(PlayerHead);
                    }
                }else{
                /*
                *  If none of the modes above is set (Empty, typos, random text, ect.) default to blacklist
                 */
                    if(config().getStringList("Main.DisabledPlayer").contains(pl.getName())){

                        // Make the playerhead and add it to the Inv.
                        ItemStack PlayerHead = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta HeadMeta = (SkullMeta)PlayerHead.getItemMeta();
                        HeadMeta.setOwningPlayer(pl.getPlayer());
                        HeadMeta.setDisplayName(pl.getName());
                        PlayerHead.setItemMeta(HeadMeta);

                        item.add(PlayerHead);
                    }

                }
            }
        }

        //ScrollerInventory(item, "", p);
    }
}

