package com.andre601.helpgui.manager;

import com.andre601.helpgui.HelpGUI;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultIntegrationManager {

    private HelpGUI plugin;

    public VaultIntegrationManager(HelpGUI plugin){
        this.plugin = plugin;
    }

    private Permission perms;

    public boolean setupPermission() {
        if(this.plugin.getServer().getPluginManager().getPlugin("Vault") == null){
            // Returning false, if Vault isn't enabled/installed
            return false;
        }
        RegisteredServiceProvider<Permission> rsp =
                this.plugin.getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return true;
    }

    // Get the primary group of the player
    public String getPrimaryGroup(Player player){
        return perms.getPrimaryGroup(player);
    }

}
