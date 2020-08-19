package me.schooltests.chatmacro.commands.context;

import me.schooltests.chatmacro.ChatMacroAPI;
import me.schooltests.chatmacro.cache.Macro;
import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;
import me.schooltests.stcf.core.command.CommandContext;
import me.schooltests.stcf.core.command.SimpleSender;
import me.schooltests.stcf.spigot.SpigotCmdUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MacroContext implements CommandContext<Macro> {
    @Override
    public Macro transform(SimpleSender simpleSender, Object prev, String value) {
        Player player = SpigotCmdUtil.toPlayer(simpleSender);
        if (player == null) return null;

        ChatMacroAPI api = Objects.requireNonNull(Bukkit.getServicesManager().getRegistration(ChatMacroAPI.class)).getProvider();
        try {
            MacroPlayer macroPlayer = api.getMacroPlayer(player.getUniqueId());
            Optional<Macro> oMacro = macroPlayer.getMacro(value.split(" ")[0]);
            return oMacro.orElse(null);
        } catch (NoSuchMacroPlayerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> tabComplete(SimpleSender simpleSender, Object o, String s) {
        Player player = SpigotCmdUtil.toPlayer(simpleSender);
        if (player == null) return new ArrayList<>();

        ChatMacroAPI api = Objects.requireNonNull(Bukkit.getServicesManager().getRegistration(ChatMacroAPI.class)).getProvider();

        try {
            MacroPlayer macroPlayer = api.getMacroPlayer(player.getUniqueId());
            return new ArrayList<>(macroPlayer.getMacros().keySet());
        } catch (NoSuchMacroPlayerException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public Class<Macro> getReturnClass() {
        return Macro.class;
    }
}
