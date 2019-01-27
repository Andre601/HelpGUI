package com.andre601.helpgui.manager;

import com.andre601.helpgui.HelpGUI;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Public class to hook into <a href="https://dev.bukkit.org/projects/vault" target="blank_">Vault</a>
 */
public class VaultManager {

    private HelpGUI plugin;

    public VaultManager(HelpGUI plugin){
        this.plugin = plugin;
    }

    private Permission perms;

    /**
     * Will let the plugin try to hook into Vault, when found.
     *
     * @return {@code true} or {@code false} depending on if Vault is on the server or not.
     */
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

    /**
     * Gets the primary group of a {@link org.bukkit.entity.Player Player} through vault.
     * @param  player
     *         The {@link org.bukkit.entity.Player Player} to get the primary group from.
     *
     * @return The name of the player's primary group.
     */
    public String getPrimaryGroup(Player player){
        return perms.getPrimaryGroup(player);
    }

}
