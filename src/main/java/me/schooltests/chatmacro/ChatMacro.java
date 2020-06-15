package me.schooltests.chatmacro;

import me.schooltests.chatmacro.commands.MacroCommand;
import me.schooltests.chatmacro.listeners.DataListener;
import me.schooltests.chatmacro.listeners.MacroListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ChatMacro extends JavaPlugin {
    private ChatMacroAPI API;

    @Override
    public void onEnable() {
        API = new ChatMacroAPI(this);
        API.loadConfig();
        API.setupStorageHandler();

        Objects.requireNonNull(getCommand("macro")).setExecutor(new MacroCommand(this));
        getServer().getPluginManager().registerEvents(new MacroListener(this), this);
        getServer().getPluginManager().registerEvents(new DataListener(this), this);

        Bukkit.getServicesManager().register(ChatMacroAPI.class, API, this, ServicePriority.Normal);
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(p -> API.saveMacroPlayer(p.getUniqueId()));
    }

    public ChatMacroAPI getAPI() {
        return API;
    }

    public void debug(String msg) {
        if (API.getConfig().getBoolean("debug-mode"))
            getLogger().warning(msg);
    }
}
