package me.schooltests.chatmacro.data;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Macro {
    private UUID owner;
    private String id;
    private List<String> macroArguments;

    private Macro(UUID owner, String id, List<String> macroArguments) {
        this.owner = owner;
        this.id = id;
        this.macroArguments = macroArguments;
    }

    private Macro(Player owner, String id, List<String> macroArguments) {
        this.owner = owner.getUniqueId();
        this.id = id;
        this.macroArguments = macroArguments;
    }

    public void execute() {

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

    public List<String> getMacroArguments() {
        return macroArguments;
    }

    public void setMacroArguments(List<String> macroArguments) {
        this.macroArguments = macroArguments;
    }
}
