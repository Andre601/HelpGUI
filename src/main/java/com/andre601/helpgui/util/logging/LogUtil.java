package com.andre601.helpgui.util.logging;

import com.andre601.helpgui.HelpGUI;
import org.bukkit.ChatColor;

public final class LogUtil {

    public static String prefix = "&f[&aHelp&2GUI&f] ";

    public static void LOG(String info){
        HelpGUI.getInstance().getServer().getConsoleSender().sendMessage(
                ChatColor.translateAlternateColorCodes('&', prefix + info)
        );
    }

}
