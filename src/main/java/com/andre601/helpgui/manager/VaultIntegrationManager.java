package com.andre601.helpgui.manager;

import com.andre601.helpgui.HelpGUI;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultIntegrationManager {

    public VaultIntegrationManager(){
    }

    private Permission perms;

    public boolean setupPermission() {
        if(HelpGUI.getInstance().getServer().getPluginManager().getPlugin("Vault") == null){
            // Returning null, if Vault isn't enabled/installed
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = HelpGUI.getInstance().getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return true;
    }

    // Get the primary group of the player
    public String getPrimaryGroup(Player player){
        return perms.getPrimaryGroup(player);
    }

}
