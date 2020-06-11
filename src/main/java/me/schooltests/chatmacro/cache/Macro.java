package me.schooltests.chatmacro.cache;

import me.schooltests.chatmacro.ChatMacroAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class Macro {
    public final UUID uniqueID = UUID.randomUUID();
    private UUID owner;
    private String name;
    private List<String> macroSteps;

    public Macro(UUID owner, String name, List<String> macroSteps) {
        this.owner = owner;
        this.name = name;
        this.macroSteps = macroSteps;
    }

    public Macro(Player owner, String name, List<String> macroSteps) {
        this.owner = owner.getUniqueId();
        this.name = name;
        this.macroSteps = macroSteps;
    }

    public void execute() {
        new BukkitRunnable() {
            public void run() {
                Player p = Bukkit.getPlayer(owner);
                if (p != null) {
                    for (String step : macroSteps) {
                        p.chat(step);
                    }
                }
            }
        }.runTask(Bukkit.getServicesManager().getRegistration(ChatMacroAPI.class).getPlugin());
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
