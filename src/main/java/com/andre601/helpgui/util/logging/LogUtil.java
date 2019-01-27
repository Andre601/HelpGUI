package com.andre601.helpgui.util.logging;

import com.andre601.helpgui.HelpGUI;
import org.bukkit.ChatColor;

public final class LogUtil {

    private HelpGUI plugin;

    public LogUtil(HelpGUI plugin){
        this.plugin = plugin;
    }

    public void info(String msg){
        plugin.getLogger().info(msg);
    }

    public void debug(String msg){
        if(!plugin.isDebug()) return;

        plugin.getLogger().info("[DEBUG] " + msg);
    }

}
