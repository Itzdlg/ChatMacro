package me.schooltests.chatmacro.commands;

import me.schooltests.chatmacro.cache.Macro;
import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;
import me.schooltests.stcf.core.command.CommandExecutor;
import me.schooltests.stcf.core.command.CommandParameter;
import me.schooltests.stcf.core.command.SimpleSender;
import me.schooltests.stcf.spigot.SpigotCmdUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListCommand extends CommandExecutor<MainCommand> {
    public ListCommand(MainCommand command) {
        super(command);
    }

    @Override
    public List<CommandParameter> getParameters() {
        return Collections.emptyList();
    }

    @Override
    public String getPermission() {
        return "chatmacro.macro.list";
    }

    @Override
    public void execute(SimpleSender sender, Map<String, Object> map) {
        Player player = SpigotCmdUtil.toPlayer(sender);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command!");
            return;
        }

        try {
            MacroPlayer macroPlayer = command.API.getMacroPlayer(player.getUniqueId());
            if (macroPlayer.getMacros().size() > 0) {
                player.sendMessage(ChatColor.GRAY + "Your macros: ");
                for (Macro m : macroPlayer.getMacros().values()) {
                    player.sendMessage(ChatColor.GOLD + "- " + ChatColor.GRAY + m.getName());
                }
            } else {
                player.sendMessage(ChatColor.GRAY + "You have no macros!");
            }
        } catch (NoSuchMacroPlayerException e) {
            e.printStackTrace();
            command.plugin.debug("No macro data was found for the player named " + player.getName() + "!");
        }
    }

    @Override
    public List<String> tabComplete(SimpleSender sender, CommandParameter commandParameter, Object o, String s) {
        return new ArrayList<>();
    }
}
