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

public class AddCommand extends CommandExecutor<MainCommand> {
    public AddCommand(MainCommand command) {
        super(command);
    }

    @Override
    public List<CommandParameter> getParameters() {
        return Arrays.asList(new CommandParameter("macro", Macro.class, true), new CommandParameter("step", String.class, true));
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
            String step = (String) map.get("step");

            MacroPlayer macroPlayer = command.API.getMacroPlayer(player.getUniqueId());

            macro.getMacroSteps().add(step);
            player.sendMessage(command.prefix + "Added step with index " + ChatColor.AQUA + macro.getMacroSteps().size() + ChatColor.WHITE + " to macro named " + ChatColor.AQUA + macro.getName() + ChatColor.WHITE + "!");
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
