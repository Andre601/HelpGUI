package com.andre601.helpgui;
import co.aikar.commands.BukkitCommandManager;
import com.andre601.helpgui.commands.CmdHelp;
import com.andre601.helpgui.commands.CmdHelpGUI;
import com.andre601.helpgui.util.ConfigUtil;
import com.andre601.helpgui.util.config.ConfigPaths;
import com.andre601.helpgui.util.logging.LogUtil;
import com.andre601.helpgui.util.players.PlayerUtil;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;

import com.andre601.helpgui.manager.VaultIntegrationManager;

import java.util.logging.Logger;

public class HelpGUIMain extends JavaPlugin {

    private static boolean VaultEnabled;
    public static HelpGUIMain instance;
    private static PluginLogger logger;
    BukkitCommandManager manager = new BukkitCommandManager(this);

    public void onEnable(){

        VaultIntegrationManager.setupPermission();

        //  Register the help-command (/help)

        instance = this;

        ConfigUtil.setupFile();

        LogUtil.INFO("Loading commands...");
        manager.registerCommand(new CmdHelp());
        manager.registerCommand(new CmdHelpGUI());

        manager.getCommandContexts().registerOptionalContext(PlayerUtil.class, c -> {
            PlayerUtil playerUtil = new PlayerUtil(this);
            playerUtil.search(c.getFirstArg() != null ? c.popFirstArg() : null);
            return playerUtil;
        });
        LogUtil.INFO("Commands registered!");

        LogUtil.INFO("Checking for Vault...");

        // Checking, if the boolean in VaultIntegrationManager returns null or not.
        if(VaultIntegrationManager.perms != null){
            VaultEnabled = true;
            LogUtil.INFO(ConfigPaths.MSG_VAULT_FOUND);
        }else{
            VaultEnabled = false;
            LogUtil.INFO(ConfigPaths.MSG_VAULT_NOT_FOUND);
        }

    }

    public void onDisable(){
        //  Unregister all the commands
        manager.unregisterCommands();
    }

    public static HelpGUIMain getInstance(){
        return instance;
    }

    public static boolean getVaultStatus(){
        return VaultEnabled;
    }

    public static Logger getLog(){
        return getInstance().getLogger();
    }

}
