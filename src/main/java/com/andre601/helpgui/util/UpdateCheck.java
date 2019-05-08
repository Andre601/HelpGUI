package com.andre601.helpgui.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to check for new versions of HelpGUI.
 *
 * Original source: https://gist.github.com/2008Choco/8ac5f78f2886d3afa23a42ed19c7ec64
 */
public class UpdateCheck {

    public static final VersionScheme VERSION_SCHEME_DECIMAL = (first, second) -> {
        String[] firstSplit = splitVersionInfo(first), secondSplit = splitVersionInfo(second);
        if(firstSplit == null || secondSplit == null) return null;

        for(int i = 0; i < Math.min(firstSplit.length, secondSplit.length); i++){
            int currValue = NumberUtils.toInt(firstSplit[i]), newestValue = NumberUtils.toInt(secondSplit[i]);

            if(newestValue > currValue){
                return second;
            }else
            if(newestValue < currValue){
                return first;
            }
        }

        return (secondSplit.length > firstSplit.length) ? second : first;
    };

    private static final String USER_AGENT = "HelpGUI-update-check";
    private static final String UPDATE_URL = "https://api.spiget.org/v2/resources/%d/versions?size=1&sort=-releaseDate";
    private static final Pattern DECIMAL_SCHEME_PATTERN = Pattern.compile("\\d+(?:\\.\\d+)*");

    private static UpdateCheck instance;

    private UpdateResult result = null;

    private final JavaPlugin PLUGIN;
    private final int PLUGIN_ID;
    private final VersionScheme VERSION_SCHEME;

    private UpdateCheck(JavaPlugin plugin, int pluginID, VersionScheme versionScheme){
        this.PLUGIN = plugin;
        this.PLUGIN_ID = pluginID;
        this.VERSION_SCHEME = versionScheme;
    }

    public CompletableFuture<UpdateResult> requestUpdateCheck(){
        return CompletableFuture.supplyAsync(() -> {
            int responseCode = -1;

            try{
                URL url = new URL(String.format(UPDATE_URL, PLUGIN_ID));
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.addRequestProperty("User-Agent", USER_AGENT);

                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                responseCode = connection.getResponseCode();

                JsonElement element = new JsonParser().parse(reader);
                if(!element.isJsonArray()){
                    return new UpdateResult(UpdateReason.INVALID_JSON);
                }

                reader.close();

                JsonObject versionObject = element.getAsJsonArray().get(0).getAsJsonObject();
                String current = PLUGIN.getDescription().getVersion(), newest = versionObject.get("name").getAsString();
                String latest = VERSION_SCHEME.compareVersions(current, newest);

                if(latest == null){
                    return new UpdateResult(UpdateReason.UNSUPPORTED_VERSION_SCHEME);
                }else
                if(latest.equals(current)){
                    return new UpdateResult(current.equals(newest) ? UpdateReason.UP_TO_DATE : UpdateReason.UNRELEASED_VERSION);
                }else
                if(latest.equals(newest)){
                    return new UpdateResult(UpdateReason.NEW_UPDATE, latest);
                }
            }catch(IOException ex){
                return new UpdateResult(UpdateReason.COULD_NOT_CONNECT);
            }catch(JsonSyntaxException ex){
                return new UpdateResult(UpdateReason.INVALID_JSON);
            }

            return new UpdateResult(responseCode == 401 ? UpdateReason.UNAUTHORIZED_QUERRY : UpdateReason.UNKNOWN_ERROR);
        });
    }

    public UpdateResult getResult(){
        return result;
    }

    private static String[] splitVersionInfo(String version){
        Matcher matcher = DECIMAL_SCHEME_PATTERN.matcher(version);
        if(!matcher.find()) return null;

        return matcher.group().split("\\.");
    }

    private static UpdateCheck init(JavaPlugin plugin, int pluginID, VersionScheme versionScheme){
        Preconditions.checkArgument(plugin != null, "Plugin cannot be null!");
        Preconditions.checkArgument(pluginID > 0, "The plugin's ID must be greater than 0!");
        Preconditions.checkArgument(versionScheme != null, "VersionScheme cannot be null!");

        return (instance == null) ? instance = new UpdateCheck(plugin, pluginID, versionScheme) : instance;
    }

    public static UpdateCheck init(JavaPlugin plugin, int pluginID){
        return init(plugin, pluginID, VERSION_SCHEME_DECIMAL);
    }

    public static UpdateCheck get(){
        Preconditions.checkState(instance != null, "Instance has not been initialized yet!");
        return instance;
    }

    public static boolean isInitialized(){
        return instance != null;
    }

    @FunctionalInterface
    public interface VersionScheme {
        /**
         * Compares two versions and returns the higher of the two.
         *
         * @param  first
         *         The first version String.
         * @param  second
         *         The second version String to compare with the first.
         *
         * @return Null if at least one of the two version Strings scheme aren't supported or the greater number.
         */
        String compareVersions(String first, String second);
    }

    public enum UpdateReason {

        /**
         * A new version of the plugin was released.
         */
        NEW_UPDATE,

        /**
         * Couldn't connect to the Spiget API.
         */
        COULD_NOT_CONNECT,

        /**
         * The JSON received from Spiget is not valid or malformed.
         */
        INVALID_JSON,

        /**
         * Spiget returned a 401 Error.
         */
        UNAUTHORIZED_QUERRY,

        /**
         * The plugin on the server has a greater version than the one available on Spigot.
         * <br>This is often the case with developement versions.
         */
        UNRELEASED_VERSION,

        /**
         * A unknown error happened.
         */
        UNKNOWN_ERROR,

        /**
         * The plugin uses a unsupported version scheme which makes it not possible to compare the two Strings.
         */
        UNSUPPORTED_VERSION_SCHEME,

        /**
         * The plugin is up to date.
         */
        UP_TO_DATE

    }

    public final class UpdateResult {
        private final UpdateReason reason;
        private final String newestVersion;

        {
            UpdateCheck.this.result = this;
        }

        private UpdateResult(UpdateReason reason, String newestVersion){
            this.reason = reason;
            this.newestVersion = newestVersion;
        }

        private UpdateResult(UpdateReason reason){
            Preconditions.checkArgument(reason != UpdateReason.NEW_UPDATE, "NEW_UPDATE requires a version!");
            this.reason = reason;
            this.newestVersion = PLUGIN.getDescription().getVersion();
        }

        /**
         * Gets the reason of this result.
         *
         * @return The {@link UpdateReason reason}
         */
        public UpdateReason getReason(){
            return reason;
        }

        /**
         * Checks if the resource has an update available.
         *
         * @return true if {@link UpdateReason reason} is {@link UpdateReason#NEW_UPDATE NEW_UPDATE}
         */
        public boolean hasUpdate(){
            return reason == UpdateReason.NEW_UPDATE;
        }

        /**
         * Gets the latest version of the plugin.
         *
         * @return Either the plugins own version ot the new version.
         */
        public String getNewestVersion() {
            return newestVersion;
        }
    }

}
