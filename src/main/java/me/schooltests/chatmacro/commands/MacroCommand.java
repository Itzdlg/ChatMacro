package me.schooltests.chatmacro.commands;

import me.schooltests.chatmacro.ChatMacro;
import me.schooltests.chatmacro.cache.Macro;
import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class MacroCommand implements CommandExecutor {
    private ChatMacro plugin;
    private String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "Chat" + ChatColor.WHITE + "Macro" + ChatColor.DARK_GRAY + "] " + ChatColor.WHITE;

    public MacroCommand(ChatMacro plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                sendHelpMessage(player);
            } else {
                if (args[0].equalsIgnoreCase("create")) {
                    if (player.hasPermission("chatmacro.macro.create")) { // Checks if the player execute the command
                        if (plugin.getAPI().canCreateNewMacro(player)) { // Checks if it's okay to create another macro with their group limit
                            if (args.length < 2) {
                                sendHelpMessage(player);
                            } else {
                                try {
                                    String macroID = args[1];
                                    MacroPlayer macroPlayer = plugin.getAPI().getMacroPlayer(player.getUniqueId());
                                    if (macroPlayer.hasMacro(macroID)) { // Does the player have an existing macro with this name
                                        player.sendMessage(prefix + "You already have a macro with that name!");
                                    } else {
                                        Macro macro = new Macro(player.getUniqueId(), macroID, new ArrayList<>());
                                        macroPlayer.addMacro(macro); // Creates macro and adds it to their player data
                                        //plugin.getAPI().saveMacroPlayer(macroPlayer); // Updates cache and pushes to storage

                                        player.sendMessage(prefix + "New macro named " + ChatColor.AQUA + macro.getName() + ChatColor.WHITE + " has been created!");
                                    }
                                } catch (NoSuchMacroPlayerException e) {  // The player's data not exist?
                                    e.printStackTrace();
                                    sendErrorMessage(player);
                                    plugin.debug("No macro data was found for the player named " + player.getName() + "!");
                                }
                            }
                        } else {
                            player.sendMessage(prefix + "You have the maximum amount of macros!");
                        }
                    } else {
                        player.sendMessage(prefix + "You are lacking the permissions to do this!");
                    }
                } else if (args[0].equalsIgnoreCase("delete")) {
                    if (player.hasPermission("chatmacro.macro.delete")) {
                        if (args.length < 2) {
                            sendHelpMessage(player);
                        } else {
                            try {
                                String macroID = args[1];
                                MacroPlayer macroPlayer = plugin.getAPI().getMacroPlayer(player.getUniqueId());
                                boolean successful = macroPlayer.removeMacro(macroID);
                                if (successful) {
                                    //plugin.getAPI().saveMacroPlayer(macroPlayer);
                                    player.sendMessage(prefix + "Macro named " + ChatColor.AQUA + macroPlayer.getMacro(macroID).get().getName() + ChatColor.WHITE + " has been deleted!");
                                } else {
                                    sendNoSuchMacroFound(player);
                                }
                            } catch (NoSuchMacroPlayerException e) {
                                e.printStackTrace();
                                sendErrorMessage(player);
                                plugin.debug("No macro data was found for the player named " + player.getName() + "!");
                            }
                        }
                    } else {
                        player.sendMessage(prefix + "You are lacking the permissions to do this!");
                    }
                } else if (args[0].equalsIgnoreCase("edit")) {
                    if (player.hasPermission("chatmacro.macro.edit")) {
                        if (args.length < 4) {
                            sendEditHelpMessage(player);
                        } else {
                            try {
                                String macroID = args[1];
                                MacroPlayer macroPlayer = plugin.getAPI().getMacroPlayer(player.getUniqueId());
                                Optional<Macro> oMacro = macroPlayer.getMacro(macroID);
                                if (oMacro.isPresent()) {
                                    Macro macro = oMacro.get();
                                    if (args[2].equalsIgnoreCase("add")) {
                                        String[] stepArr = Arrays.copyOfRange(args, 3, args.length);
                                        String step = String.join(" ", stepArr);
                                        macro.getMacroSteps().add(step);
                                        //plugin.getAPI().saveMacroPlayer(macroPlayer);

                                        player.sendMessage(prefix + "Added step with index " + ChatColor.AQUA + macro.getMacroSteps().size() + ChatColor.WHITE + " to macro named " + ChatColor.AQUA + macro.getName() + ChatColor.WHITE + "!");
                                    } else if (args[2].equalsIgnoreCase("addn")) {
                                        if (args.length < 5) {
                                            sendEditHelpMessage(player);
                                        } else {
                                            try {
                                                int pos = Integer.valueOf(args[3]);
                                                String[] stepArr = Arrays.copyOfRange(args, 4, args.length);
                                                String step = String.join(" ", stepArr);
                                                macro.getMacroSteps().add(pos - 1, step);
                                                //plugin.getAPI().saveMacroPlayer(macroPlayer);

                                                player.sendMessage(prefix + "Added step with index " + pos + ChatColor.WHITE + " to macro named " + ChatColor.AQUA + macro.getName() + ChatColor.WHITE + "!");
                                            } catch (NumberFormatException e) {
                                                player.sendMessage(prefix + ChatColor.RED + "Please use an actual number for the position!");
                                            }
                                        }
                                    } else if (args[2].equalsIgnoreCase("remove")) {
                                        try {
                                            int pos = Integer.valueOf(args[3]);
                                            if (macro.getMacroSteps().size() >= pos) {
                                                macro.getMacroSteps().remove(pos - 1);
                                                //plugin.getAPI().saveMacroPlayer(macroPlayer);

                                                player.sendMessage(prefix + "Removed step from macro named " + ChatColor.AQUA + macro.getName() + ChatColor.WHITE + "!");
                                            } else {
                                                player.sendMessage(prefix + ChatColor.RED + "That step position does not exist in this macro!");
                                            }
                                        } catch (NumberFormatException e) {
                                            player.sendMessage(prefix + ChatColor.RED + "Please use an actual number for the position!");
                                        }
                                    } else {
                                        sendEditHelpMessage(player);
                                    }
                                } else {
                                    sendNoSuchMacroFound(player);
                                }
                            } catch (NoSuchMacroPlayerException e) {
                                e.printStackTrace();
                                sendErrorMessage(player);
                                plugin.debug("No macro data was found for the player named " + player.getName() + "!");
                            }
                        }
                    } else {
                        player.sendMessage(prefix + "You are lacking the permissions to do this!");
                    }
                } else if (args[0].equalsIgnoreCase("info")) {
                    if (player.hasPermission("chatmacro.macro.info")) {
                        if (args.length < 2) {
                            sendEditHelpMessage(player);
                        } else {
                            try {
                                String macroID = args[1];
                                MacroPlayer macroPlayer = plugin.getAPI().getMacroPlayer(player.getUniqueId());
                                Optional<Macro> oMacro = macroPlayer.getMacro(macroID);
                                if (oMacro.isPresent()) {
                                    Macro macro = oMacro.get();
                                    player.sendMessage("");
                                    player.sendMessage(prefix + "Viewing macro named " + ChatColor.AQUA + macro.getName() + ChatColor.WHITE + "!");
                                    for (String step : macro.getMacroSteps()) {
                                        player.sendMessage(ChatColor.AQUA + "Step #" + (macro.getMacroSteps().indexOf(step) + 1)+ ": " + ChatColor.WHITE + step);
                                    }

                                    player.sendMessage("");
                                } else {
                                    sendNoSuchMacroFound(player);
                                }
                            } catch (NoSuchMacroPlayerException e) {
                                e.printStackTrace();
                                sendErrorMessage(player);
                                plugin.debug("No macro data was found for the player named " + player.getName() + "!");
                            }
                        }
                    } else {
                        player.sendMessage(prefix + "You are lacking the permissions to do this!");
                    }
                } else if (args[0].equalsIgnoreCase("list")) {
                    try {
                        MacroPlayer macroPlayer = plugin.getAPI().getMacroPlayer(player.getUniqueId());
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
                        sendErrorMessage(player);
                        plugin.debug("No macro data was found for the player named " + player.getName() + "!");
                    }
                } else {
                    sendHelpMessage(player);
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You must execute this command as a player!");
        }

        return true;
    }

    private void sendHelpMessage(Player p) {
        p.sendMessage(ChatColor.AQUA + "/macro <create/delete/edit/info/list> [macro]");
        p.sendMessage(ChatColor.AQUA + "Ex. /macro create " + ChatColor.WHITE + " welcome");
    }

    private void sendEditHelpMessage(Player p) {
        p.sendMessage(ChatColor.WHITE + "To add a step to your macro use " + ChatColor.AQUA + "/macro edit [name] add [step]");
        p.sendMessage(ChatColor.WHITE + "To add a step to your macro in a set position use " + ChatColor.AQUA + "/macro edit [name] addn [step number] [step]");
        p.sendMessage(ChatColor.WHITE + "To remove a step from your macro use " + ChatColor.AQUA + "/macro edit [name] remove [step number]");
        p.sendMessage(ChatColor.WHITE + "To show the steps of your macro use" + ChatColor.AQUA + "/macro info [name]");
        p.sendMessage(ChatColor.AQUA + "[step]" + ChatColor.WHITE + " = The command or message to execute");
        p.sendMessage(ChatColor.AQUA + "[step number]" + ChatColor.WHITE + " = The position of the step found in /macro info");
    }

    private void sendErrorMessage(Player p) {
        p.sendMessage(ChatColor.RED + "Something went wrong! Ask an administrator for more information");
    }

    private void sendNoSuchMacroFound(Player p) {
        p.sendMessage(prefix + "You do not have a macro with that name!");
    }
}
