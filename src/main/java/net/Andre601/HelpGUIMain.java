package net.Andre601;

import com.google.common.io.ByteStreams;
import org.bukkit.configuration.file.FileConfiguration;
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

        // Checking, if the boolean in VaultIntegrationManager returns null or not.
        if(VaultIntegrationManager.perms != null){
            VaultEnabled = true;
            System.out.println("Found Vault. group-search enabled.");
        }else{
            VaultEnabled = false;
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

    public void setupFiles(){

        //  Create Datafolder ("plugins/HelpGUI") if it doesn't exist.
        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        File cfg = new File(getDataFolder(), "config.yml");

        if(!cfg.exists()){
            try{

                // Creating new file and adding the default stuff to it.
                cfg.createNewFile();
                InputStream is = getClass().getResourceAsStream("config.yml");
                OutputStream os = new FileOutputStream(cfg);

                ByteStreams.copy(is, os);

                // Closing for safety.
                os.close();
            }catch (Exception e){
                throw new RuntimeException("Unable to create config.yml", e);
            }
        }
    }

    public static FileConfiguration Config() {
        return HelpGUIMain.getInstance().getConfig();
    }


}
