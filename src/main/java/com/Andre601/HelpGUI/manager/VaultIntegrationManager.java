package com.andre601.helpgui.manager;

import com.andre601.helpgui.HelpGUIMain;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultIntegrationManager {

    public static Permission perms;

    public static boolean setupPermission() {
        if(HelpGUIMain.getInstance().getServer().getPluginManager().getPlugin("Vault") == null){
            // Returning null, if Vault isn't enabled/installed
            return perms == null;
        }
        RegisteredServiceProvider<Permission> rsp = HelpGUIMain.getInstance().getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    // Get the primary group of the player
    public static String getPrimaryGroup(Player player){
        return perms.getPrimaryGroup(player);
    }

}
