package com.andre601.helpgui.util.logging;

import com.andre601.helpgui.HelpGUI;
import org.bukkit.ChatColor;

public final class LogUtil {

    private HelpGUI plugin;

    public LogUtil(HelpGUI plugin){
        this.plugin = plugin;
    }


    public void LOG(String info){
        this.plugin.getServer().getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&',
                        "&f[&aHelp&2GUI&f] " + info
                )
        );
    }

}
