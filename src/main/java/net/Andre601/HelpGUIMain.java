package net.Andre601;
import net.Andre601.util.ConfigUtil;
import org.bukkit.plugin.java.JavaPlugin;

import net.Andre601.Managers.VaultIntegrationManager;

public class HelpGUIMain extends JavaPlugin {

    private static boolean VaultEnabled;
    public static HelpGUIMain instance;

    public void onEnable(){

        VaultIntegrationManager.setupPermission();

        // Checking, if the boolean in VaultIntegrationManager returns null or not.
        if(VaultIntegrationManager.perms != null){
            VaultEnabled = true;
            System.out.println("Found Vault. group-search enabled.");
        }else{
            VaultEnabled = false;
            System.out.println("No Vault found");
        }
        instance = this;

        ConfigUtil.setupFile();
    }

    public static HelpGUIMain getInstance(){
        return instance;
    }

    public static boolean getVaultStatus(){
        return VaultEnabled;
    }


}
