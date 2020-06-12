package me.schooltests.chatmacro.cache;

import me.schooltests.chatmacro.ChatMacroAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings("unused")
public class Macro {
    private UUID uniqueID = UUID.randomUUID();
    private UUID owner;
    private String name;
    private List<String> macroSteps;

    public Macro(UUID owner, String name, List<String> macroSteps) {
        this.owner = owner;
        this.name = name;
        this.macroSteps = macroSteps;
    }

    public Macro(UUID owner, String name, List<String> macroSteps, UUID referenceID) {
        this.owner = owner;
        this.name = name;
        this.macroSteps = macroSteps;
        this.uniqueID = referenceID;
    }

    public void execute(String[] args) {
        new BukkitRunnable() {
            public void run() {
                Player p = Bukkit.getPlayer(owner);
                if (p != null) {
                    for (String step : macroSteps) {
                        for (int i = 0; i < args.length; i++) step = step.replaceAll("\\{arg-" + i + "}", args[i]);
                        p.chat(step);
                    }
                }
            }
        }.runTask(Objects.requireNonNull(Bukkit.getServicesManager().getRegistration(ChatMacroAPI.class)).getPlugin());
    }

    public UUID getUniqueID() {
        return uniqueID;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMacroSteps() {
        return macroSteps;
    }

    public void setMacroSteps(List<String> macroSteps) {
        this.macroSteps = macroSteps;
    }
}
