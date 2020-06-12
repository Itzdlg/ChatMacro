package me.schooltests.chatmacro;

import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;
import me.schooltests.chatmacro.storage.JSONHandler;
import me.schooltests.chatmacro.storage.SQLHandler;
import me.schooltests.chatmacro.storage.StorageHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * ChatMacroAPI contains all methods for the plugin and
 * other developers to manipulate the plugin and data
 *
 * @author Dominick
 * @version 1.0
 */
public class ChatMacroAPI {
    enum StorageType { JSON, SQLite, MySQL }
    private ChatMacro plugin;
    private HashMap<UUID, MacroPlayer> cache = new HashMap<>();
    private StorageType storageType;
    private boolean useMacroLimits = false;
    private YamlConfiguration config = new YamlConfiguration();

    private StorageHandler storageHandler;

    ChatMacroAPI(ChatMacro plugin) {
        this.plugin = plugin;
    }

    /**
     * @return The ChatMacro instance that instantiated this API
     */
    @SuppressWarnings("unused")
    public ChatMacro getPlugin() {
        return plugin;
    }

    /**
     * Loads the configuration file, and if it doesn't
     * exist it copies the default configuration.
     */
    void loadConfig() {
        try {
            File file = new File(plugin.getDataFolder(), "config.yml");
            if (!file.exists()) {
                final boolean success = file.getParentFile().mkdirs();
                if (success) plugin.saveResource("config.yml", false);
            }

            config.load(file);

            if (!config.contains("debug-mode")) {
                config.set("debug-mode", false);
                plugin.debug("Error finding config value debug-mode, setting to FALSE");
            }

            String rawDataType = config.getString("data-type");
            plugin.debug("Raw data type: " + rawDataType);
            if (rawDataType == null) {
                config.set("data-type", "JSON");
                storageType = StorageType.JSON;
                plugin.debug("Error finding config value data-type, setting to JSON");
            } else if (rawDataType.equalsIgnoreCase("json")) {
                plugin.debug("Setting to JSON");
                storageType = StorageType.JSON;
            } else if (rawDataType.equalsIgnoreCase("sqlite")) {
                plugin.debug("Setting to SQLite");
                storageType = StorageType.SQLite;
            } else if (rawDataType.equalsIgnoreCase("mysql")) {
                plugin.debug("Setting to MySQL");
                storageType = StorageType.MySQL;
            } else {
                storageType = StorageType.JSON;
                plugin.debug("Incorrect value for config value data-type, defaulting to JSON");
            }

            useMacroLimits = config.contains("macro-limits.enabled") && config.getBoolean("macro-limits.enabled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the storage system and prepare it for work
     */
    void setupStorageHandler() {
        switch (storageType) {
            case JSON:
                storageHandler = new JSONHandler();
                break;
            case SQLite:
                storageHandler = new SQLHandler();
                break;
            case MySQL:
                storageHandler = new SQLHandler(
                        config.getString("connection-params.address"),
                        config.getString("connection-params.database"),
                        config.getString("connection-params.username"),
                        config.getString("connection-params.password")
                );
                break;
        }

        storageHandler.setup();
    }

    /**
     * Checks whether or not a player can create a new macro with group limits
     * @param p Player to check
     * @return If the player can create a new macro within their group limit
     */
    public boolean canCreateNewMacro(Player p) {
        if (p.hasPermission("chatmacro.limit.none") || !useMacroLimits) return true;
        try {
            int currentMacroCount = getMacroPlayer(p.getUniqueId()).getMacros().size();
            int defaultLimit = config.getInt("macro-limits.groups.default");
            if (currentMacroCount < defaultLimit) return true;
            for (String key : Objects.requireNonNull(config.getConfigurationSection("macro-limits.groups")).getKeys(false)) {
                plugin.debug("Looping through macro limit groups on index: " + key);
                if (key.equalsIgnoreCase("default")) continue;
                int macroLimit = config.getInt("macro-limits.groups." + key);
                plugin.debug("Current index's macro limit: " + macroLimit);
                if (currentMacroCount < macroLimit && p.hasPermission("chatmacro.limit." + key)) return true;
            }

            return false;
        } catch (NoSuchMacroPlayerException e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * @return The currently loaded configuration from cache.
     */
    @SuppressWarnings("WeakerAccess")
    public YamlConfiguration getConfig() {
        return config;
    }

    /**
     * Retrieves the plauer's data and caches it as MacroPlayer
     * @see MacroPlayer
     * @param uuid The player's UUID that needs to be cached
     */
    public void cachePlayerData(UUID uuid) {
        try {
            cache.put(uuid, getMacroPlayer(uuid));
        } catch (NoSuchMacroPlayerException e) {
            MacroPlayer macroPlayer = new MacroPlayer(uuid, new HashMap<>());
            cache.put(uuid, macroPlayer);
        }
    }

    /**
     * Saves a MacroPlayer object to storage
     * @see MacroPlayer
     * @param macroPlayer The MacroPlayer to be saved
     */
    private void saveMacroPlayer(MacroPlayer macroPlayer) {
        storageHandler.put(macroPlayer);
    }

    /**
     * Saves user data to storage
     * @param u The user to be saved
     */
    public void saveMacroPlayer(UUID u) {
        try {
            saveMacroPlayer(getMacroPlayer(u));
        } catch (NoSuchMacroPlayerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves data from storage if not available in cache
     * @see MacroPlayer
     * @param uuid The player's UUID that the data is saved under
     * @return The retrieved data as a MacroPlayer
     * @throws NoSuchMacroPlayerException The data is missing from storage
     */
    public MacroPlayer getMacroPlayer(UUID uuid) throws NoSuchMacroPlayerException {
        if (cache.containsKey(uuid)) {
            return cache.get(uuid);
        } else {
            return storageHandler.get(uuid);
        }
    }
}
