package net.Andre601;

import com.google.common.io.ByteStreams;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.Andre601.Managers.VaultIntegrationManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class HelpGUIMain extends JavaPlugin {

    private static boolean VaultEnabled;
    public static HelpGUIMain instance;

    public void onEnable(){

        VaultIntegrationManager.setupPermission();

        if(VaultIntegrationManager.perms != null){
            VaultEnabled = false;
            System.out.println("Found Vault. group-search enabled.");
        }else{
            VaultEnabled = true;
            System.out.println("No Vault found");
        }
        instance = this;

        setupFiles();
    }

    public static HelpGUIMain getInstance(){
        return instance;
    }

    public static boolean getVaultStatus(){
        return VaultEnabled;
    }
    HelpGUIMain main;

    public void setupFiles(){
        if(!main.getInstance().getDataFolder().exists()) main.getInstance().getDataFolder().mkdir();

        File cfg = new File(main.getDataFolder(), "config.yml");

        if(!cfg.exists()){
            try{
                cfg.createNewFile();
                InputStream is = getClass().getResourceAsStream("config.yml");
                OutputStream os = new FileOutputStream(cfg);

                ByteStreams.copy(is, os);
            }catch (Exception e){
                throw new RuntimeException("Unable to create config.yml", e);
            }
        }
    }
}
