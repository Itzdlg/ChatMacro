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

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class InfoCommand extends CommandExecutor<MainCommand> {
    public InfoCommand(MainCommand command) {
        super(command);
    }

    @Override
    public List<CommandParameter> getParameters() {
        return Collections.singletonList(new CommandParameter("macro", Macro.class, true));
    }

    @Override
    public String getPermission() {
        return "chatmacro.macro.info";
    }

    @Override
    public void execute(SimpleSender sender, Map<String, Object> map) {
        Player player = SpigotCmdUtil.toPlayer(sender);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command!");
            return;
        }

        Macro macro = (Macro) map.get("macro");

        player.sendMessage("");
        player.sendMessage(command.prefix + "Viewing macro named " + ChatColor.AQUA + macro.getName() + ChatColor.WHITE + "!");
        for (String step : macro.getMacroSteps()) {
            player.sendMessage(ChatColor.AQUA + "Step #" + (macro.getMacroSteps().indexOf(step) + 1)+ ": " + ChatColor.WHITE + step);
        }

        player.sendMessage("");
    }

    @Override
    public List<String> tabComplete(SimpleSender sender, CommandParameter commandParameter, Object o, String s) {
        return manager.getRegisteredContext(commandParameter.contextClass).tabComplete(sender, o, s);
    }
}
