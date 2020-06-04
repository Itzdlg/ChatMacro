package me.schooltests.chatmacro;

import me.schooltests.chatmacro.data.Macro;
import me.schooltests.chatmacro.data.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * ChatMacroAPI contains all methods for the plugin and
 * other developers to manipulate the plugin and data
 *
 * @author Dominick
 * @version 1.0
 */
public class ChatMacroAPI {
    private ChatMacro plugin;
    private HashMap<UUID, MacroPlayer> cache = new HashMap<UUID, MacroPlayer>();
    private StorageType storageType;
    private YamlConfiguration config = new YamlConfiguration();

    public ChatMacroAPI(ChatMacro plugin) {
        this.plugin = plugin;
    }

    /**
     * @return The ChatMacro instance that instantiated this API
     */
    public ChatMacro getPlugin() {
        return plugin;
    }

    /**
     * Loads the configuration file, and if it doesn't
     * exist it copies the default configuration.
     */
    public void loadConfig() {
        try {
            File file = new File(plugin.getDataFolder(), "config.yml");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                plugin.saveResource("config.yml", false);
                config.load(file);

                if (!config.contains("debug-mode")) {
                    config.set("debug-mode", false);
                    plugin.debug("Error finding config value debug-mode, setting to FALSE");
                }

                String rawDataType = config.getString("data-type");
                if (rawDataType == null) {
                    config.set("data-type", "JSON");
                    storageType = StorageType.JSON;
                    plugin.debug("Error finding config value data-type, setting to JSON");
                } else if (rawDataType.equalsIgnoreCase("json")) {
                    storageType = StorageType.JSON;
                } else if (rawDataType.equalsIgnoreCase("sqlite")) {
                    storageType = StorageType.SQLite;
                } else if (rawDataType.equalsIgnoreCase("mysql")) {
                    storageType = StorageType.MySQL;
                } else {
                    storageType = StorageType.JSON;
                    plugin.debug("Incorrect value for config value data-type, defaulting to JSON");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return The currently loaded configuration from cache.
     */
    public YamlConfiguration getConfig() {
        return config;
    }

    /**
     * Retrieves the plauer's data and caches it as MacroPlayer
     * @see MacroPlayer
     * @param uuid The player's UUID that needs to be cached
     */
    public void cacheMacroPlayer(UUID uuid) {
        try {
            cache.put(uuid, getMacroPlayer(uuid));
        } catch (NoSuchMacroPlayerException e) {
            MacroPlayer macroPlayer = new MacroPlayer(uuid, new ArrayList<Macro>());
            cache.put(uuid, macroPlayer);
        }
    }

    /**
     * Saves a MacroPlayer object to storage
     * @see MacroPlayer
     * @param macroPlayer The MacroPlayer to be saved
     */
    public void saveMacroPlayer(MacroPlayer macroPlayer) {
        // TODO: Save the data to storage
    }

    /**
     * Retrieves data from storage if not available in cache
     * @see MacroPlayer
     * @param uuid The player's UUID that the data is saved under
     * @return The retrieved data as a MacroPlayer
     * @throws NoSuchMacroPlayerException
     */
    public MacroPlayer getMacroPlayer(UUID uuid) throws NoSuchMacroPlayerException {
        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        } else {
            // TODO: Read from storage and return data
        }

        throw new NoSuchMacroPlayerException();
    }
}
