package com.andre601.helpgui.util.config;

import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.ConfigPaths;
import com.andre601.helpgui.util.logging.LogUtil;
import com.google.common.io.ByteStreams;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ConfigUtil {

    public static void setupFile(){
        LogUtil.INFO("&7Loading config.yml...");
        HelpGUI.getInstance().saveDefaultConfig();
        LogUtil.INFO("&7Config successfully loaded!");
    }

    public static List<String> getStringList(String path){
        return config().getStringList(path);
    }

    public static FileConfiguration config() {
        return HelpGUI.getInstance().getConfig();
    }

    public static String color(String path){
        return ChatColor.translateAlternateColorCodes('&', config().getString(path));
    }

    public static String transformCol(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> getColoredList(List<String> string){
        List<String> colored = new ArrayList<>();
        for(String s : string){
            colored.add(transformCol(s));
        }
        return colored;
    }
}
