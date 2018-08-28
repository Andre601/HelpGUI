package net.Andre601.util;

import com.google.common.io.ByteStreams;
import net.Andre601.HelpGUIMain;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ConfigUtil {

    public static void setupFile(){

        //  Create Datafolder ("plugins/HelpGUI") if it doesn't exist.
        if(!HelpGUIMain.getInstance().getDataFolder().exists())
            HelpGUIMain.getInstance().getDataFolder().mkdir();

        File cfg = new File(HelpGUIMain.getInstance().getDataFolder(), "config.yml");

        //  If the file doesn't exist, start to create one. Else: Do nothing.
        if(!cfg.exists()){
            try{

                // Creating new file and adding the default stuff to it.
                cfg.createNewFile();
                InputStream is = HelpGUIMain.getInstance().getClass().getResourceAsStream("config.yml");
                OutputStream os = new FileOutputStream(cfg);

                ByteStreams.copy(is, os);

                // Closing for safety.
                os.close();
            }catch (Exception e){
                throw new RuntimeException("Unable to create config.yml", e);
            }
        }
    }

    public static List<?> getList(String path){
        return config().getList(path);
    }

    public static List<String> getStringList(String path){
        return config().getStringList(path);
    }

    public static FileConfiguration config() {
        return HelpGUIMain.getInstance().getConfig();
    }


    public static String color(String string){
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String getMultiLineString(String path){
        StringBuilder sb = new StringBuilder();
        getList(path).forEach(string -> sb.append(string).append("\n"));
        return color(sb.toString());
    }
}
