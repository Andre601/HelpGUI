package com.andre601.helpgui.util.logging;

import com.andre601.helpgui.HelpGUI;

public final class LogUtil {

    public static void INFO(String info){
        HelpGUI.getInstance().getLogger().info(info);
    }

    public static void WARN(String warn){
        HelpGUI.getInstance().getLogger().warning(warn);
    }

}
