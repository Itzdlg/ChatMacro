package me.schooltests.chatmacro;

import com.google.common.util.concurrent.Runnables;
import me.schooltests.chatmacro.commands.MacroCommand;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;
import me.schooltests.chatmacro.listeners.DataListener;
import me.schooltests.chatmacro.listeners.MacroListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChatMacro extends JavaPlugin {
    private ChatMacroAPI API;

    public void onEnable() {
        API = new ChatMacroAPI(this);
        API.loadConfig();
        API.loadDependencies();

        getCommand("macro").setExecutor(new MacroCommand(this));
        getServer().getPluginManager().registerEvents(new MacroListener(this), this);
        getServer().getPluginManager().registerEvents(new DataListener(this), this);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Bukkit.getOnlinePlayers().forEach(p -> {
                try {
                    API.saveMacroPlayer(API.getMacroPlayer(p.getUniqueId()));
                } catch (NoSuchMacroPlayerException e) {
                    e.printStackTrace();
                }
            });
        }));

        Bukkit.getServicesManager().register(ChatMacroAPI.class, API, this, ServicePriority.Normal);
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
