package me.schooltests.chatmacro.data;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Macro {
    private UUID owner;
    private String id;
    private List<String> macroSteps;

    public Macro(UUID owner, String id, List<String> macroSteps) {
        this.owner = owner;
        this.id = id;
        this.macroSteps = macroSteps;
    }

    public Macro(Player owner, String id, List<String> macroSteps) {
        this.owner = owner.getUniqueId();
        this.id = id;
        this.macroSteps = macroSteps;
    }

    public void execute() {
        Player p = Bukkit.getPlayer(owner);
        if (p != null) {
            for (String step : macroSteps) {
                p.chat(step);
            }
        }
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMacroSteps() {
        return macroSteps;
    }

    public void setMacroSteps(List<String> macroSteps) {
        this.macroSteps = macroSteps;
    }
}
