package me.schooltests.chatmacro;

import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;
import me.schooltests.chatmacro.storage.JSONHandler;
import me.schooltests.chatmacro.storage.StorageType;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
    private HashMap<UUID, MacroPlayer> cache = new HashMap<>();
    private StorageType storageType = StorageType.JSON;
    private boolean useMacroLimits = false;
    private YamlConfiguration config = new YamlConfiguration();

    private Permission vaultPermissionHandler;

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
                boolean success = file.getParentFile().mkdirs();
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
     * Loads plugin dependencies
     */
    public void loadDependencies() {
        // Load Vault Permissions
        if (config.contains("macro-limits") && config.contains("macro-limits.enabled")) {
            if (config.getBoolean("macro-limits.enabled")) {
                RegisteredServiceProvider<Permission> registeredServiceProvider = plugin.getServer().getServicesManager().getRegistration(Permission.class);
                if (registeredServiceProvider == null) {
                    plugin.debug("Failed to load Vault, defaulting to NO MACRO LIMITS");
                } else {
                    vaultPermissionHandler = registeredServiceProvider.getProvider();
                    useMacroLimits = true;
                }
            }
        }
    }

    /**
     * @return The vault permission handler if available
     */
    public Optional<Permission> getVaultPermissionHandler() {
        if (vaultPermissionHandler != null) {
            return Optional.of(vaultPermissionHandler);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Checks whether or not a player can create a new macro with group limits
     * @param p Player to check
     * @return If the player can create a new macro within their group limit
     */
    public boolean canCreateNewMacro(Player p) {
        if (!useMacroLimits || !getVaultPermissionHandler().isPresent() || p.hasPermission("chatmacro.macro.unlimited")) {
            return true;
        } else {
            List<String> playerGroups = Arrays.asList(vaultPermissionHandler.getPlayerGroups(p));
            List<Integer> groupLimits = new ArrayList<>();
            playerGroups.stream()
                    .filter(group -> config.contains("macro-limits.groups." + group))
                    .forEach(i -> groupLimits.add(config.getInt("macro-limits.groups." + i)));

            if (groupLimits.isEmpty()) return true;
            try {
                MacroPlayer macroPlayer = getMacroPlayer(p.getUniqueId());
                int maxLimit = Collections.max(groupLimits);
                return macroPlayer.getMacros().size() < maxLimit;
            } catch (NoSuchMacroPlayerException e) {
                e.printStackTrace();
                return true;
            }

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
    public void saveMacroPlayer(MacroPlayer macroPlayer) {
        // TODO: Save the data to storage
        if (storageType == StorageType.JSON) {
            // JSONHandler
        } else if (storageType == StorageType.SQLite) {
            // SQLHandler
        } else if (storageType == StorageType.MySQL) {
            // SQLHandler(mysql)
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
            // TODO: Read from storage and return data
        }

        throw new NoSuchMacroPlayerException();
    }
}
