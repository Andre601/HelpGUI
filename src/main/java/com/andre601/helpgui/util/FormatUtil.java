package com.andre601.helpgui.util;

import com.andre601.helpgui.HelpGUI;
import com.andre601.helpgui.util.config.ConfigKey;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FormatUtil {

    private HelpGUI plugin;
    private final Pattern COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf("&") + "[0-9A-FK-OR]");

    public FormatUtil(HelpGUI plugin){
        this.plugin = plugin;
    }

    public ItemStack getItem(ConfigKey path){
        Material material = Material.matchMaterial(plugin.getConfig().getString(path.getPath()));

        if(material == null){
            material = Material.SIGN;
            plugin.getLogUtil().debug(String.format(
                    "A item-name was invalid! Path: %s",
                    path.path
            ));
        }

        return new ItemStack(material);
    }

    public List<String> formatLore(OfflinePlayer player, ConfigKey path){
        List<String> coloredLore = new ArrayList<>();

        for(String line : plugin.getConfig().getStringList(path.getPath()))
            coloredLore.add(formatText(player, line));

        return coloredLore;
    }

    public String formatText(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public String formatText(OfflinePlayer player, ConfigKey path){
        String text = formatText(plugin.getConfig().getString(path.getPath()));

        return plugin.isPlaceholderAPIEnabled() ? PlaceholderAPI.setPlaceholders(player, text) : text;
    }

    public String formatText(ConfigKey path){
        return formatText(plugin.getConfig().getString(path.getPath()));
    }

    private String formatText(OfflinePlayer player, String text){
        text = formatText(text);

        return plugin.isPlaceholderAPIEnabled() ? PlaceholderAPI.setPlaceholders(player, text) : text;
    }

    public void sendMsg(Player player, ConfigKey path){
        sendMsg(player, plugin.getConfig().getString(path.getPath()));
    }

    public void sendMsg(Player player, ConfigKey path, String target, String replacement){
        sendMsg(player, plugin.getConfig().getString(path.getPath()).replace(target, replacement));
    }

    private void sendMsg(Player player, String msg){
        String prefix = formatText(plugin.getConfig().getString(ConfigKey.INV_TITLE.getPath()));
        msg = formatText(player, msg);

        player.sendMessage(prefix + msg);
    }

    private String stripColor(String msg){
        return COLOR_PATTERN.matcher(msg).replaceAll("");
    }

    public String stripColor(ConfigKey configKey){
        return stripColor(plugin.getConfig().getString(configKey.getPath()));
    }

    public String stripColor(ConfigKey configKey, String target, String replacement){
        return stripColor(plugin.getConfig().getString(configKey.getPath()).replace(target, replacement));
    }

    public String color(String msg){
        char[] c = msg.toCharArray();

        for(int i = 0; i < c.length; ++i){
            if(c[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(c[i + 1]) > -1){
                c[i] = 167;
                c[i + 1] = Character.toLowerCase(c[i + 1]);
            }
        }

        return new String(c);
    }
}
