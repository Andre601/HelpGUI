package com.andre601.helpgui.util.logging;

import com.andre601.helpgui.HelpGUIMain;

public final class LogUtil {

    public static void INFO(String info){
        HelpGUIMain.getInstance().getLogger().info(info);
    }

    public static void WARN(String warn){
        HelpGUIMain.getInstance().getLogger().warning(warn);
    }

}
