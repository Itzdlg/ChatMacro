package me.schooltests.chatmacro.listeners;

import me.schooltests.chatmacro.ChatMacro;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataListener implements Listener {
    private ChatMacro plugin;
    public DataListener(ChatMacro plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        plugin.debug(event.getPlayer().getName() + " has joined, caching data");
        plugin.getAPI().cachePlayerData(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(PlayerQuitEvent event) {
        plugin.debug(event.getPlayer().getName() + " has left, saving data");
        plugin.getAPI().saveMacroPlayer(event.getPlayer().getUniqueId());
    }
}
