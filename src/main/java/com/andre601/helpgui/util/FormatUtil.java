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

public class FormatUtil {

    private HelpGUI plugin;

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

    private String formatText(String text){
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
}
