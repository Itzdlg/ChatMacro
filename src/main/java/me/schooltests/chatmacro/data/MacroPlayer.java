package me.schooltests.chatmacro.data;

import java.util.List;
import java.util.UUID;

public class MacroPlayer {
    private UUID uuid;
    private List<Macro> macros;

    public MacroPlayer(UUID uuid, List<Macro> macros) {
        this.uuid = uuid;
        this.macros = macros;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<Macro> getMacros() {
        return macros;
    }

    public void setMacros(List<Macro> macros) {
        this.macros = macros;
    }
}
