package me.schooltests.chatmacro.commands.edit;

import me.schooltests.chatmacro.cache.Macro;
import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.commands.MainCommand;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;
import me.schooltests.stcf.core.command.CommandExecutor;
import me.schooltests.stcf.core.command.CommandParameter;
import me.schooltests.stcf.core.command.SimpleSender;
import me.schooltests.stcf.spigot.SpigotCmdUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RemoveStepCommand extends CommandExecutor<MainCommand> {
    public RemoveStepCommand(MainCommand command) {
        super(command);
    }

    @Override
    public List<CommandParameter> getParameters() {
        return Arrays.asList(new CommandParameter("macro", Macro.class, true), new CommandParameter("position", Integer.class, true));
    }

    @Override
    public String getPermission() {
        return "chatmacro.macro.edit";
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
            Integer pos = (Integer) map.get("position");

            MacroPlayer macroPlayer = command.API.getMacroPlayer(player.getUniqueId());
            if (macro.getMacroSteps().size() >= pos) {
                macro.getMacroSteps().remove(pos - 1);
                player.sendMessage(command.prefix + "Removed step from macro named " + ChatColor.AQUA + macro.getName() + ChatColor.WHITE + "!");
            } else {
                player.sendMessage(command.prefix + ChatColor.RED + "That step position does not exist in this macro!");
            }
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