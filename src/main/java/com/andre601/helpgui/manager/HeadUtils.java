package com.andre601.helpgui.manager;

import com.andre601.helpgui.util.config.ConfigKey;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * Original work by darbyjack
 *
 * Original source: https://github.com/darbyjack/Guilds-Plugi
 */
public class HeadUtils {

    public static Map<UUID, String> textures = new HashMap<>();

    public static String getURL(UUID uuid){
        if (textures.containsKey(uuid)) return textures.get(uuid);

        try {
            URL textureURL = new URL("https://api.minetools.eu/profile/" + uuid.toString().replaceAll("-", ""));
            InputStreamReader inputStream = new InputStreamReader(textureURL.openStream());
            JsonObject textureProperty = new JsonParser().parse(inputStream)
                    .getAsJsonObject().get("decoded")
                    .getAsJsonObject().get("textures")
                    .getAsJsonObject().get("SKIN")
                    .getAsJsonObject();
            String texture = textureProperty.get("url").getAsString();
            textures.put(uuid, texture);

            return texture;

        }catch(IOException ex) {
            return ConfigKey.DEFAULT_HEAD_TEXTURE.getString(false);
        }
    }

    public static ItemStack getSkull(String url){
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);

        if(url.isEmpty()) return head;

        ItemMeta meta = head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field field = null;
        try{
            field = meta.getClass().getDeclaredField("profile");
        }catch(NoSuchFieldException | SecurityException ex){
            ex.printStackTrace();
        }
        field.setAccessible(true);
        try{
            field.set(meta, profile);
        }catch(IllegalArgumentException | IllegalAccessException ex){
            ex.printStackTrace();
        }
        head.setItemMeta(meta);
        return head;
    }

}
