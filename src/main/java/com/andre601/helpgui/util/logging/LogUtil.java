package com.andre601.helpgui.util.logging;

import com.andre601.helpgui.HelpGUI;
import org.bukkit.ChatColor;

public final class LogUtil {

    private HelpGUI plugin;

    public LogUtil(HelpGUI plugin){
        this.plugin = plugin;
    }

    private static String prefix = "&f[&aHelp&2GUI&f] ";

    public void LOG(String info){
        this.plugin.getServer().getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', prefix + info)
        );
    }

}
