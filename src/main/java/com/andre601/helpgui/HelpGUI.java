package com.andre601.helpgui;
import co.aikar.commands.BukkitCommandManager;
import com.andre601.helpgui.commands.CmdHelp;
import com.andre601.helpgui.commands.CmdHelpGUI;
import com.andre601.helpgui.manager.EventManager;
import com.andre601.helpgui.manager.ScrollerInventory;
import com.andre601.helpgui.util.FormatUtil;
import com.andre601.helpgui.util.config.ConfigKey;
import com.andre601.helpgui.util.logging.LogUtil;
import com.andre601.helpgui.util.players.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.andre601.helpgui.manager.VaultManager;

import java.util.ArrayList;


public class HelpGUI extends JavaPlugin {

    private boolean vaultEnabled;
    private boolean placheholderAPIEnabled;
    private boolean debug;
    private BukkitCommandManager manager;

    private LogUtil logUtil;
    private ScrollerInventory scrollerInventory;
    private VaultManager vaultManager;
    private FormatUtil formatUtil;

    public void onEnable(){

        long startTime = System.currentTimeMillis();

        ConfigKey.plugin = this;
        logUtil = new LogUtil(this);
        vaultManager = new VaultManager(this);
        formatUtil = new FormatUtil(this);

        saveDefaultConfig();

        debug = getConfig().getBoolean(ConfigKey.DEBUG.getKey(), false);

        PluginManager pluginManager = Bukkit.getPluginManager();

        if(getConfig().getBoolean(ConfigKey.SHOW_BANNER.getKey(), true))
            sendBanner();

        loadCommands(this);

        logUtil.debug("Register events...");
        pluginManager.registerEvents(new EventManager(this), this);

        logUtil.info("Checking for Vault...");
        checkVaultStatus();

        logUtil.info("Checking for PlaceholderAPI...");
        if(pluginManager.getPlugin("PlaceholderAPI") != null){
            placheholderAPIEnabled = true;
            logUtil.info(getConfig().getString(ConfigKey.MSG_PAPI_FOUND.getKey()));
        }else{
            placheholderAPIEnabled = false;
            logUtil.info(getConfig().getString(ConfigKey.MSG_PAPI_NOT_FOUND.getKey()));
        }

        logUtil.info("Plugin enabled in " + getTime(startTime) + "ms!");
    }

    public void onDisable(){
        //  Unregister all the commands
        unloadCommands();
        logUtil.info("HelpGUI disabled! Good bye.");
    }

    public boolean getVaultStatus(){
        return vaultEnabled;
    }

    private void checkVaultStatus(){
        if(vaultManager.setupPermission()){
            vaultEnabled = true;
            logUtil.info(getConfig().getString(ConfigKey.MSG_VAULT_FOUND.getKey()));
        }else{
            vaultEnabled = false;
            logUtil.info(getConfig().getString(ConfigKey.MSG_VAULT_NOT_FOUND.getKey()));
        }
    }

    private void loadCommands(HelpGUI plugin){
        manager = new BukkitCommandManager(this);

        logUtil.debug("Register Command Contexts...");
        manager.getCommandContexts().registerOptionalContext(PlayerUtil.class, c -> {
            PlayerUtil playerUtil = new PlayerUtil(this);
            playerUtil.search(c.getPlayer(), c.getFirstArg() != null ? c.popFirstArg() : null);
            return playerUtil;
        });

        manager.enableUnstableAPI("help");

        logUtil.debug("Registering commands...");
        manager.registerCommand(new CmdHelp(plugin));
        manager.registerCommand(new CmdHelpGUI(plugin));
    }

    private void unloadCommands(){
        //  The manager is already registered, so we don't have to worry...
        logUtil.debug("Unload Commands...");
        manager.unregisterCommands();
        logUtil.debug("Commands unloaded.");
    }

    private long getTime(long startTime){
        return System.currentTimeMillis() - startTime;
    }

    private void sendBanner(){
        System.out.println("§7");
        System.out.println("§a _   _  §2  ____");
        System.out.println("§a| | | | §2 / ___)");
        System.out.println("§a| |_| | §2| /  _");
        System.out.println("§a|_____| §2|_| (_|");
        System.out.println("§a _   _  §2 _____");
        System.out.println("§a|_| |_| §2 \\___/");
        System.out.println("§7");
    }

    public boolean isPlaceholderAPIEnabled(){
        return placheholderAPIEnabled;
    }

    public LogUtil getLogUtil(){
        return logUtil;
    }

    public void setScrollerInventory(ArrayList<ItemStack> items, String title, Player player){
        scrollerInventory = new ScrollerInventory(this, items, title, player);
    }

    public ScrollerInventory getScrollerInventory(){
        return scrollerInventory;
    }

    public VaultManager getVaultManager(){
        return vaultManager;
    }

    public FormatUtil getFormatUtil(){
        return formatUtil;
    }

    public boolean isDebug() {
        return debug;
    }
}
