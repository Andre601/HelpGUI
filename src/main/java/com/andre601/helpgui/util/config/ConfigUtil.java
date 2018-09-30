package com.andre601.helpgui.util.config;

import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.logging.LogUtil;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtil {

    public static void setupFile(){
        LogUtil.LOG("&7Loading config.yml...");
        HelpGUI.getInstance().saveDefaultConfig();
        LogUtil.LOG("&7Config successfully loaded!");
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
    public static List<String> getColoredList(List<String> string, Player player){
        List<String> colored = new ArrayList<>();
        for(String s : string){
            if(HelpGUI.getPlaceholderAPIStatus())
                colored.add(transformCol(PlaceholderAPI.setPlaceholders(player, s)));
            else
                colored.add(transformCol(s));
        }
        return colored;
    }
}
