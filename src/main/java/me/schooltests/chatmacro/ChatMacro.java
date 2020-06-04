package me.schooltests.chatmacro;

import org.bukkit.plugin.java.JavaPlugin;

public class ChatMacro extends JavaPlugin {
    private static ChatMacro instance;
    private ChatMacroAPI API;

    public void onEnable() {
        instance = this;
        API = new ChatMacroAPI(this);
        API.loadConfig();
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
