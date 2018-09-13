package com.andre601.helpgui;
import co.aikar.commands.BukkitCommandManager;
import com.andre601.helpgui.commands.CmdHelp;
import com.andre601.helpgui.commands.CmdHelpGUI;
import com.andre601.helpgui.manager.EventManager;
import com.andre601.helpgui.util.config.ConfigUtil;
import com.andre601.helpgui.util.config.ConfigPaths;
import com.andre601.helpgui.util.logging.LogUtil;
import com.andre601.helpgui.util.players.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.andre601.helpgui.manager.VaultIntegrationManager;

import static com.andre601.helpgui.util.config.ConfigUtil.color;
import static com.andre601.helpgui.util.config.ConfigUtil.config;

public class HelpGUI extends JavaPlugin {

    private static boolean vaultEnabled;
    public static HelpGUI instance;
    BukkitCommandManager manager;

    public void onEnable(){

        long startTime = System.currentTimeMillis();

        instance = this;

        sendBanner();

        ConfigUtil.setupFile();
        loadCommands();

        LogUtil.INFO("&7Register events...");
        Bukkit.getPluginManager().registerEvents(new EventManager(), this);
        LogUtil.INFO("&7Events successfully registered!");

        LogUtil.INFO("&7Checking for Vault...");
        checkVaultStatus();

        LogUtil.INFO("&7Plugin enabled in " + getTime(startTime) + "ms!");
    }

    public void onDisable(){
        //  Unregister all the commands
        unloadCommands();
        LogUtil.INFO("&7HelpGUI disabled! Good bye.");
    }

    public static HelpGUI getInstance(){
        return instance;
    }

    public static boolean getVaultStatus(){
        return vaultEnabled;
    }

    public void checkVaultStatus(){
        if(VaultIntegrationManager.setupPermission()){
            vaultEnabled = true;
            LogUtil.INFO(config().getString(ConfigPaths.MSG_VAULT_FOUND));
        }else{
            vaultEnabled = false;
            LogUtil.WARN(config().getString(ConfigPaths.MSG_VAULT_NOT_FOUND));
        }
    }

    private void loadCommands(){
        manager = new BukkitCommandManager(this);

        LogUtil.INFO("&7Register Command Contexts...");
        manager.getCommandContexts().registerOptionalContext(PlayerUtil.class, c -> {
            PlayerUtil playerUtil = new PlayerUtil(getInstance());
            playerUtil.search(c.getPlayer(), c.getFirstArg() != null ? c.popFirstArg() : null);
            return playerUtil;
        });
        LogUtil.INFO("&7Command Contexts successfully loaded!");

        manager.enableUnstableAPI("help");

        LogUtil.INFO("&7Registering commands...");
        manager.registerCommand(new CmdHelp());
        manager.registerCommand(new CmdHelpGUI());
        LogUtil.INFO("&7Commands successfully loaded!");
    }

    private void unloadCommands(){
        //  The manager is already registered, so we don't have to worry...
        LogUtil.INFO("&7Unload Commands...");
        manager.unregisterCommands();
        LogUtil.INFO("&7Commands unloaded.");
    }

    private long getTime(long startTime){
        return System.currentTimeMillis() - startTime;
    }

    private void sendBanner(){
        LogUtil.INFO("");
        LogUtil.INFO("&a _   _  &2  ____");
        LogUtil.INFO("&a| | | | &2 / ___)");
        LogUtil.INFO("&a| |_| | &2| /  _");
        LogUtil.INFO("&a|_____| &2|_| (_|");
        LogUtil.INFO("&a _   _  &2 _____");
        LogUtil.INFO("&a|_| |_| &2 \\___/");
        LogUtil.INFO("");
    }

    public String prefix(){
        return color(ConfigPaths.MSG_INV_TITLE);
    }
}
