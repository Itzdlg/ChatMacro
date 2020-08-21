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

public class AddnCommand extends CommandExecutor<MainCommand> {
    public AddnCommand(MainCommand command) {
        super(command);
    }

    @Override
    public List<CommandParameter> getParameters() {
        return Arrays.asList(new CommandParameter("macro", Macro.class, true),
                new CommandParameter("position", Integer.class, true),
                new CommandParameter("step", String.class, true));
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
            String step = (String) map.get("step");

            MacroPlayer macroPlayer = command.API.getMacroPlayer(player.getUniqueId());

            macro.getMacroSteps().add(pos - 1, step);
            player.sendMessage(command.prefix + "Added step with index " + pos + ChatColor.WHITE + " to macro named " + ChatColor.AQUA + macro.getName() + ChatColor.WHITE + "!");
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