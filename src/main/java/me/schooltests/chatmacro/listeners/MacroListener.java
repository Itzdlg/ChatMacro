package me.schooltests.chatmacro.listeners;

import me.schooltests.chatmacro.ChatMacro;
import me.schooltests.chatmacro.cache.Macro;
import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Optional;

public class MacroListener implements Listener {
    private ChatMacro plugin;

    public MacroListener(ChatMacro plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMacroUse(AsyncPlayerChatEvent event) {
        if (event.getMessage().startsWith("$")) {
            try {
                String macroID = event.getMessage().split(" ")[0].replaceFirst("\\$", "");
                MacroPlayer macroPlayer = plugin.getAPI().getMacroPlayer(event.getPlayer().getUniqueId());
                Optional<Macro> oMacro = macroPlayer.getMacro(macroID);
                if (oMacro.isPresent()) {
                    event.setCancelled(true);
                    Macro macro = oMacro.get();
                    macro.execute(event.getMessage().replaceFirst("\\$" + macroID, "").split(" "));
                }
            } catch (NoSuchMacroPlayerException e) {
                e.printStackTrace();
            }
        }
    }
}
