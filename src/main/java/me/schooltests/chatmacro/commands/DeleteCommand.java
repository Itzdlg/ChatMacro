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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DeleteCommand extends CommandExecutor<MainCommand> {
    public DeleteCommand(MainCommand command) {
        super(command);
    }

    @Override
    public List<CommandParameter> getParameters() {
        return Collections.singletonList(new CommandParameter("macro", Macro.class, true));
    }

    @Override
    public String getPermission() {
        return "chatmacro.macro.delete";
    }

    @Override
    public void execute(SimpleSender sender, Map<String, Object> map) {
        Player player = SpigotCmdUtil.toPlayer(sender);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command!");
            return;
        }

        try {
            Macro macro = (Macro) map.get("macro");
            MacroPlayer macroPlayer = command.API.getMacroPlayer(player.getUniqueId());
            boolean successful = macroPlayer.removeMacro(macro.getName());
            if (successful)
                player.sendMessage(command.prefix + "Macro named " + ChatColor.AQUA + macro.getName() + ChatColor.WHITE + " has been deleted!");
            else
                player.sendMessage(command.prefix + "You don't have a macro with this name!");
        } catch (NoSuchMacroPlayerException e) {
            e.printStackTrace();
            command.plugin.debug("No macro data was found for the player named " + player.getName() + "!");
        }
    }

    @Override
    public List<String> tabComplete(SimpleSender sender, CommandParameter commandParameter, Object o, String s) {
        return manager.getRegisteredContext(commandParameter.contextClass).tabComplete(sender, o, s);
    }
}
