package net.Andre601.Managers;

import net.Andre601.HelpGUIMain;
import net.Andre601.util.config.ConfigPaths;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

import static net.Andre601.util.ConfigUtil.*;

public class InventoryManager {

    List<ItemStack> item = new ArrayList<>();

    // The two inventories, that will be used.
    private Inventory HelpInv = null;
    private Inventory SearchInv = null;

    private VaultIntegrationManager vault;
    private HelpGUIMain main;

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

    // SearchInv for /help [Name/group:[Group]]
    public void CreateSearchInv(Player p, String args){

        SearchInv = p.getPlayer().getServer().createInventory(null, 54, "SearchInv");

        // Creating the deco-Item (Black stained Glasspane)
        ItemStack Deco = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta DecoMeta = Deco.getItemMeta();
        DecoMeta.setDisplayName("§8");
        Deco.setItemMeta(DecoMeta);

        //Creating the Info-Item (Book)
        ItemStack Info = new ItemStack(Material.BOOK);
        ItemMeta InfoMeta = Info.getItemMeta();
        InfoMeta.setDisplayName("Click on a playerhead");
        Info.setItemMeta(InfoMeta);

        SearchInv.setItem(4, Info);

        for(Player pl : Bukkit.getOnlinePlayers()){
            if(pl != p){
                // Checking, if the arg starts with "group:"
                if(args.startsWith("group:")){
                    /*
                    * Get the actual group of the player.
                    * If the group equals the searched group: Create playerhead
                     */
                    if(main.getVaultStatus()){
                        if(vault.getGroup(pl).equalsIgnoreCase(args.replace("group:", ""))){
                            ItemStack PlayerHead = new ItemStack(Material.PLAYER_HEAD);
                            SkullMeta HeadMeta = (SkullMeta)PlayerHead.getItemMeta();
                            HeadMeta.setOwningPlayer(pl.getPlayer());
                            HeadMeta.setDisplayName(pl.getName());
                            PlayerHead.setItemMeta(HeadMeta);

                            HelpInv.addItem(PlayerHead);
                        }
                    }else{
                        p.sendMessage(config().getString(color(ConfigPaths.ERR_VAULT_NOT_ENABLED)));
                    }
                }else{
                    /*
                    * If the playername starts with the provided argument: Create playerhead
                    * That way, players only need to type "/help And" to search for Andre_601.
                    * Good for complicated names.
                     */
                    if(pl.getName().startsWith(args)){
                        // Make the playerhead and add it to the Inv.
                        ItemStack PlayerHead = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta HeadMeta = (SkullMeta)PlayerHead.getItemMeta();
                        HeadMeta.setOwningPlayer(pl.getPlayer());
                        HeadMeta.setDisplayName(pl.getName());
                        PlayerHead.setItemMeta(HeadMeta);

                        HelpInv.addItem(PlayerHead);
                    }
                }
            }
        }
        p.openInventory(SearchInv);
    }
}

