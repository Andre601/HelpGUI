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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.andre601.helpgui.manager.VaultIntegrationManager;

import static com.andre601.helpgui.util.config.ConfigUtil.color;
import static com.andre601.helpgui.util.config.ConfigUtil.config;

public class HelpGUI extends JavaPlugin {

    private static boolean vaultEnabled;
    private static boolean papiEnabled;
    private static HelpGUI instance;
    private BukkitCommandManager manager;

    public void onEnable(){

        long startTime = System.currentTimeMillis();

        instance = this;
        ConfigUtil.setupFile();

        PluginManager pluginManager = Bukkit.getPluginManager();

        if(ConfigUtil.config().getBoolean(ConfigPaths.SHOW_BANNER))
            sendBanner();

        loadCommands();

        LogUtil.LOG("&7Register events...");
        pluginManager.registerEvents(new EventManager(), this);
        LogUtil.LOG("&7Events successfully registered!");

        LogUtil.LOG("&7Checking for Vault...");
        checkVaultStatus();

        LogUtil.LOG("&7Checking for PlaceholderAPI...");
        if(pluginManager.getPlugin("PlaceholderAPI") != null){
            papiEnabled = true;
            LogUtil.LOG(config().getString(ConfigPaths.MSG_PAPI_FOUND));
        }else{
            papiEnabled = false;
            LogUtil.LOG(config().getString(ConfigPaths.MSG_PAPI_NOT_FOUND));
        }

        LogUtil.LOG("&7Plugin enabled in " + getTime(startTime) + "ms!");
    }

    public void onDisable(){
        //  Unregister all the commands
        unloadCommands();
        LogUtil.LOG("&7HelpGUI disabled! Good bye.");
    }

    public static HelpGUI getInstance(){
        return instance;
    }

    public static boolean getVaultStatus(){
        return vaultEnabled;
    }

    private void checkVaultStatus(){
        if(VaultIntegrationManager.setupPermission()){
            vaultEnabled = true;
            LogUtil.LOG(config().getString(ConfigPaths.MSG_VAULT_FOUND));
        }else{
            vaultEnabled = false;
            LogUtil.LOG(config().getString(ConfigPaths.MSG_VAULT_NOT_FOUND));
        }
    }

    private void loadCommands(){
        manager = new BukkitCommandManager(this);

        LogUtil.LOG("&7Register Command Contexts...");
        manager.getCommandContexts().registerOptionalContext(PlayerUtil.class, c -> {
            PlayerUtil playerUtil = new PlayerUtil(getInstance());
            playerUtil.search(c.getPlayer(), c.getFirstArg() != null ? c.popFirstArg() : null);
            return playerUtil;
        });
        LogUtil.LOG("&7Command Contexts successfully loaded!");

        manager.enableUnstableAPI("help");

        LogUtil.LOG("&7Registering commands...");
        manager.registerCommand(new CmdHelp());
        manager.registerCommand(new CmdHelpGUI());
        LogUtil.LOG("&7Commands successfully loaded!");
    }

    private void unloadCommands(){
        //  The manager is already registered, so we don't have to worry...
        LogUtil.LOG("&7Unload Commands...");
        manager.unregisterCommands();
        LogUtil.LOG("&7Commands unloaded.");
    }

    private long getTime(long startTime){
        return System.currentTimeMillis() - startTime;
    }

    private void sendBanner(){
        LogUtil.LOG("");
        LogUtil.LOG("&a _   _  &2  ____");
        LogUtil.LOG("&a| | | | &2 / ___)");
        LogUtil.LOG("&a| |_| | &2| /  _");
        LogUtil.LOG("&a|_____| &2|_| (_|");
        LogUtil.LOG("&a _   _  &2 _____");
        LogUtil.LOG("&a|_| |_| &2 \\___/");
        LogUtil.LOG("");
    }

    public String prefix(){
        return color(ConfigPaths.MSG_INV_TITLE);
    }

    public static boolean getPlaceholderAPIStatus(){
        return papiEnabled;
    }
}
