package com.andre601.helpgui.util.logging;

import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.ConfigUtil;

public final class LogUtil {

    public static String prefix = "&f[&aHelp&2GUI&f] ";

    public static void INFO(String info){
        HelpGUI.getInstance().getServer().getConsoleSender().sendMessage(ConfigUtil.transformCol(prefix + info));
    }

    public static void WARN(String warn){
        HelpGUI.getInstance().getServer().getConsoleSender().sendMessage(ConfigUtil.transformCol(prefix + warn));
    }

}
