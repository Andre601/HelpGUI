package net.Andre601.Managers;

import net.Andre601.HelpGUIMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class InventoryManager {

    // The two inventories, that will be used.
    private Inventory HelpInv = null;
    private Inventory SearchInv = null;

    private VaultIntegrationManager vault;
    private HelpGUIMain main;

    // HelpInv for /help
    public void CreateHelpInv(Player p){

        // Creating the actual HelpInv
        HelpInv = p.getPlayer().getServer().createInventory(null, 45, "HelpGUI");

        // Creating the deco-Item (Black stained Glasspane)
        ItemStack Deco = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
        ItemMeta DecoMeta = Deco.getItemMeta();
        DecoMeta.setDisplayName("§8");
        Deco.setItemMeta(DecoMeta);

        //Creating the Info-Item (Book)
        ItemStack Info = new ItemStack(Material.BOOK);
        ItemMeta InfoMeta = Info.getItemMeta();
        InfoMeta.setDisplayName("Click on a playerhead");
        Info.setItemMeta(InfoMeta);

        HelpInv.setItem(4, Info);

        for(Player pl : Bukkit.getOnlinePlayers()){
            if(pl != p){
                // Make the playerhead and add it to the Inv.
                ItemStack PlayerHead = new ItemStack(Material.SKULL_ITEM, 1, (byte)3);
                SkullMeta HeadMeta = (SkullMeta)PlayerHead.getItemMeta();
                HeadMeta.setOwner(pl.getName());
                HeadMeta.setDisplayName(pl.getName());
                PlayerHead.setItemMeta(HeadMeta);

                HelpInv.addItem(PlayerHead);
            }
        }

        p.openInventory(HelpInv);
    }

    // SearchInv for /help [Name/group:[Group]]
    public void CreateSearchInv(Player p, String args){

        SearchInv = p.getPlayer().getServer().createInventory(null, 54, "SearchInv");

        // Creating the deco-Item (Black stained Glasspane)
        ItemStack Deco = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte)15);
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
                if(args.startsWith("groups")){
                    /*
                    * Get the actual group of the player.
                    * If the group equals the searched group: Create playerhead
                     */
                    if(main.getVaultStatus()){
                        if(vault.getGroup(pl).equalsIgnoreCase(args.replace("group:", ""))){
                            ItemStack PlayerHead = new ItemStack(Material.SKULL_ITEM, 1, (byte)3);
                            SkullMeta HeadMeta = (SkullMeta)PlayerHead.getItemMeta();
                            HeadMeta.setOwner(pl.getName());
                            HeadMeta.setDisplayName(pl.getName());
                            PlayerHead.setItemMeta(HeadMeta);

                            HelpInv.addItem(PlayerHead);
                        }
                    }else{
                        p.sendMessage("Vault isn not installed/enabled on the server!");
                    }
                }else{
                    /*
                    * If the playername starts with the argument args: Create playerhead
                    * That way, players only need to type "/help And" to search for Andre_601.
                    * Good for complicated names.
                     */
                    if(pl.getName().startsWith(args)){
                        // Make the playerhead and add it to the Inv.
                        ItemStack PlayerHead = new ItemStack(Material.SKULL_ITEM, 1, (byte)3);
                        SkullMeta HeadMeta = (SkullMeta)PlayerHead.getItemMeta();
                        HeadMeta.setOwner(pl.getName());
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

