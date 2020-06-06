package me.schooltests.chatmacro.listeners;

import me.schooltests.chatmacro.ChatMacro;
import me.schooltests.chatmacro.data.Macro;
import me.schooltests.chatmacro.data.MacroPlayer;
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
        if (event.getMessage().startsWith("$") && event.getMessage().split(" ").length == 1) {
            try {
                String macroID = event.getMessage().replaceFirst("\\$", "");
                MacroPlayer macroPlayer = plugin.getAPI().getMacroPlayer(event.getPlayer().getUniqueId());
                Optional<Macro> oMacro = macroPlayer.getMacro(macroID);
                if (oMacro.isPresent()) {
                    Macro macro = oMacro.get();
                    macro.execute();
                }
            } catch (NoSuchMacroPlayerException e) {
                e.printStackTrace();
            }
        }
    }
}
