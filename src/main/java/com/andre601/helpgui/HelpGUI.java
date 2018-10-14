package com.andre601.helpgui;
import co.aikar.commands.BukkitCommandManager;
import com.andre601.helpgui.commands.CmdHelp;
import com.andre601.helpgui.commands.CmdHelpGUI;
import com.andre601.helpgui.manager.EventManager;
import com.andre601.helpgui.manager.ScrollerInventory;
import com.andre601.helpgui.util.config.ConfigKey;
import com.andre601.helpgui.util.logging.LogUtil;
import com.andre601.helpgui.util.players.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.andre601.helpgui.manager.VaultIntegrationManager;

public class HelpGUI extends JavaPlugin {

    private boolean vaultEnabled;
    private boolean papiEnabled;
    private BukkitCommandManager manager;

    private static HelpGUI instance;
    private LogUtil logUtil;
    private ScrollerInventory scrollerInventory;
    private VaultIntegrationManager vaultIntegrationManager;

    public void onEnable(){

        long startTime = System.currentTimeMillis();

        instance = this;
        logUtil.LOG("&7Loading config.yml...");
        saveDefaultConfig();
        logUtil.LOG("&7Config successfully loaded!");

        logUtil = new LogUtil(this);
        scrollerInventory = new ScrollerInventory();
        vaultIntegrationManager = new VaultIntegrationManager(this);

        PluginManager pluginManager = Bukkit.getPluginManager();

        if(ConfigKey.SHOW_BANNER.getBoolean())
            sendBanner();

        loadCommands(this);

        logUtil.LOG("&7Register events...");
        pluginManager.registerEvents(new EventManager(this), this);
        logUtil.LOG("&7Events successfully registered!");

        logUtil.LOG("&7Checking for Vault...");
        checkVaultStatus();

        logUtil.LOG("&7Checking for PlaceholderAPI...");
        if(pluginManager.getPlugin("PlaceholderAPI") != null){
            papiEnabled = true;
            logUtil.LOG(ConfigKey.MSG_PAPI_FOUND.getString(true));
        }else{
            papiEnabled = false;
            logUtil.LOG(ConfigKey.MSG_PAPI_NOT_FOUND.getString(true));
        }

        logUtil.LOG("&7Plugin enabled in " + getTime(startTime) + "ms!");
    }

    public void onDisable(){
        //  Unregister all the commands
        unloadCommands();
        logUtil.LOG("&7HelpGUI disabled! Good bye.");
    }

    public static HelpGUI getInstance(){
        return instance;
    }

    public boolean getVaultStatus(){
        return vaultEnabled;
    }

    private void checkVaultStatus(){
        if(vaultIntegrationManager.setupPermission()){
            vaultEnabled = true;
            logUtil.LOG(ConfigKey.MSG_VAULT_FOUND.getString(true));
        }else{
            vaultEnabled = false;
            logUtil.LOG(ConfigKey.MSG_VAULT_NOT_FOUND.getString(true));
        }
    }

    private void loadCommands(HelpGUI plugin){
        manager = new BukkitCommandManager(this);

        logUtil.LOG("&7Register Command Contexts...");
        manager.getCommandContexts().registerOptionalContext(PlayerUtil.class, c -> {
            PlayerUtil playerUtil = new PlayerUtil(this);
            playerUtil.search(c.getPlayer(), c.getFirstArg() != null ? c.popFirstArg() : null);
            return playerUtil;
        });
        logUtil.LOG("&7Command Contexts successfully loaded!");

        manager.enableUnstableAPI("help");

        logUtil.LOG("&7Registering commands...");
        manager.registerCommand(new CmdHelp());
        manager.registerCommand(new CmdHelpGUI(plugin));
        logUtil.LOG("&7Commands successfully loaded!");
    }

    private void unloadCommands(){
        //  The manager is already registered, so we don't have to worry...
        logUtil.LOG("&7Unload Commands...");
        manager.unregisterCommands();
        logUtil.LOG("&7Commands unloaded.");
    }

    private long getTime(long startTime){
        return System.currentTimeMillis() - startTime;
    }

    private void sendBanner(){
        logUtil.LOG("");
        logUtil.LOG("&a _   _  &2  ____");
        logUtil.LOG("&a| | | | &2 / ___)");
        logUtil.LOG("&a| |_| | &2| /  _");
        logUtil.LOG("&a|_____| &2|_| (_|");
        logUtil.LOG("&a _   _  &2 _____");
        logUtil.LOG("&a|_| |_| &2 \\___/");
        logUtil.LOG("");
    }

    public boolean getPlaceholderAPIStatus(){
        return papiEnabled;
    }

    public LogUtil getLogUtil(){
        return logUtil;
    }

    public ScrollerInventory getScrollerInventory(){
        return scrollerInventory;
    }

    public VaultIntegrationManager getVaultIntegrationManager(){
        return vaultIntegrationManager;
    }
}
