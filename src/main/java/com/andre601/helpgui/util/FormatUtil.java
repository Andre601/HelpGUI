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

    public ItemStack getItem(String path){
        Material material = Material.matchMaterial(plugin.getConfig().getString(path));

        if(material == null){
            material = Material.SIGN;
            plugin.getLogUtil().debug(String.format(
                    "A item-name was invalid! Path: %s",
                    path
            ));
        }

        return new ItemStack(material);
    }

    public List<String> formatLore(OfflinePlayer player, String path){
        List<String> coloredLore = new ArrayList<>();

        for(String line : plugin.getConfig().getStringList(path))
            coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));

        return plugin.isPlaceholderAPIEnabled() ? PlaceholderAPI.setPlaceholders(player, coloredLore) : coloredLore;
    }

    public String formatText(OfflinePlayer player, String path){
        String text = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(path));

        return plugin.isPlaceholderAPIEnabled() ? PlaceholderAPI.setPlaceholders(player, text) : text;
    }

    public String formatText(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public void sendMessage(Player player, String text){
        text = PlaceholderAPI.setPlaceholders(player, text);

        player.sendMessage(formatText(plugin.getConfig().getString(ConfigKey.INV_TITLE.getPath()) + text));
    }
}
