package me.schooltests.chatmacro;

import me.schooltests.chatmacro.commands.MacroCommand;
import me.schooltests.chatmacro.listeners.DataListener;
import me.schooltests.chatmacro.listeners.MacroListener;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatMacro extends JavaPlugin {
    private static ChatMacro instance;
    private ChatMacroAPI API;

    public void onEnable() {
        instance = this;
        API = new ChatMacroAPI(this);
        API.loadConfig();
        API.loadDependencies();

        getCommand("macro").setExecutor(new MacroCommand(this));
        getServer().getPluginManager().registerEvents(new MacroListener(this), this);
        getServer().getPluginManager().registerEvents(new DataListener(this), this);
    }

    public static ChatMacro getInstance() {
        return instance;
    }

    public ChatMacroAPI getAPI() {
        return API;
    }

    public void debug(String msg) {
        debug(msg, false);
    }

    public void debug(String msg, boolean force) {
        if (API.getConfig().getBoolean("debug-mode") || force) {
            getLogger().warning(msg);
        }
    }
}
