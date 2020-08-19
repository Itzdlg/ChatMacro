package me.schooltests.chatmacro.commands;

import me.schooltests.chatmacro.ChatMacro;
import me.schooltests.chatmacro.ChatMacroAPI;
import me.schooltests.chatmacro.cache.Macro;
import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;
import me.schooltests.stcf.core.command.CommandExecutor;
import me.schooltests.stcf.core.command.CommandParameter;
import me.schooltests.stcf.core.command.SimpleSender;
import me.schooltests.stcf.spigot.SpigotCmdUtil;
import me.schooltests.stcf.spigot.SpigotSTCommandManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CreateCommand extends CommandExecutor<MainCommand> {
    public CreateCommand(MainCommand command) {
        super(command);
    }

    @Override
    public List<CommandParameter> getParameters() {
        return Collections.singletonList(new CommandParameter("name", String.class, true));
    }

    @Override
    public String getPermission() {
        return "chatmacro.macro.create";
    }

    @Override
    public void execute(SimpleSender sender, Map<String, Object> map) {
        Player player = SpigotCmdUtil.toPlayer(sender);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Only players may use this command!");
            return;
        }

        if (command.API.canCreateNewMacro(player)) { // Checks if it's okay to create another macro with their group limit
            try {
                String macroID = (String) map.get("name");
                MacroPlayer macroPlayer = command.API.getMacroPlayer(player.getUniqueId());
                Macro macro = new Macro(player.getUniqueId(), macroID, new ArrayList<>());
                boolean successful = macroPlayer.addMacro(macro);
                if (successful)
                    player.sendMessage(command.prefix + "New macro named " + ChatColor.AQUA + macro.getName() + ChatColor.WHITE + " has been created!");
                else
                    player.sendMessage(command.prefix + "You already have a macro with that name!");
            } catch (NoSuchMacroPlayerException e) {  // The player's data not exist?
                e.printStackTrace();
                command.plugin.debug("No macro data was found for the player named " + player.getName() + "!");
            }
        } else sender.sendMessage(command.prefix + "You can not create any more macros");
    }

    @Override
    public List<String> tabComplete(SimpleSender sender, CommandParameter commandParameter, Object o, String s) {
        return new ArrayList<>();
    }
}
