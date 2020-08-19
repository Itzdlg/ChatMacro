package me.schooltests.chatmacro.commands;

import me.schooltests.chatmacro.ChatMacro;
import me.schooltests.chatmacro.ChatMacroAPI;
import me.schooltests.chatmacro.commands.context.MacroContext;
import me.schooltests.chatmacro.commands.edit.AddCommand;
import me.schooltests.chatmacro.commands.edit.AddnCommand;
import me.schooltests.chatmacro.commands.edit.RemoveStepCommand;
import me.schooltests.stcf.core.STCommandManager;
import me.schooltests.stcf.core.command.CommandExecutor;
import me.schooltests.stcf.core.command.CommandParameter;
import me.schooltests.stcf.core.command.STCommand;
import me.schooltests.stcf.core.command.SimpleSender;
import me.schooltests.stcf.spigot.SpigotSTCommandManager;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainCommand extends STCommand {
    public final String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Chat" + ChatColor.WHITE + "Macro" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;
    public final ChatMacro plugin;
    public final ChatMacroAPI API;

    public MainCommand(STCommandManager manager) {
        super("macro", manager);
        plugin = ((ChatMacro) ((SpigotSTCommandManager) manager).plugin);
        API = plugin.getAPI();

        manager.registerContext(new MacroContext());

        executor = new CommandExecutor<MainCommand>(this) {
            @Override
            public void execute(SimpleSender sender, Map<String, Object> map) {
                sender.sendMessage(ChatColor.WHITE + "To add a step to your macro use " + ChatColor.AQUA + "/macro edit add [macro] [step]");
                sender.sendMessage(ChatColor.WHITE + "To add a step to your macro in a set position use " + ChatColor.AQUA + "/macro edit addn [macro] [step number] [step]");
                sender.sendMessage(ChatColor.WHITE + "To remove a step from your macro use " + ChatColor.AQUA + "/macro edit remove [macro] [step number]");
                sender.sendMessage(ChatColor.WHITE + "To show the steps of your macro use" + ChatColor.AQUA + "/macro info [name]");
                sender.sendMessage(ChatColor.AQUA + "[step]" + ChatColor.WHITE + " = The command or message to execute");
                sender.sendMessage(ChatColor.AQUA + "[step number]" + ChatColor.WHITE + " = The position of the step found in /macro info");
                sender.sendMessage("\n" + ChatColor.WHITE + "- NOTE: You can use {arg-#} as a placeholder for when executing the macro in chat");
            }

            @Override
            public List<String> tabComplete(SimpleSender sender, CommandParameter commandParameter, Object o, String s) {
                return new ArrayList<>();
            }
        };

        subExecutors.put("create", new CreateCommand(this));
        subExecutors.put("delete", new DeleteCommand(this));

        subExecutors.put("edit add", new AddCommand(this));
        subExecutors.put("edit addn", new AddnCommand(this));
        subExecutors.put("edit remove", new RemoveStepCommand(this));

        subExecutors.put("info", new InfoCommand(this));
        subExecutors.put("list", new ListCommand(this));

        description = "The main command to modify player macros";
        aliases.add("chatmacro");
    }
}