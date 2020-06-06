package me.schooltests.chatmacro.data;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MacroPlayer {
    private UUID uuid;
    private Map<String, Macro> macros;

    public MacroPlayer(UUID uuid, Map<String, Macro> macros) {
        this.uuid = uuid;
        this.macros = macros;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Map<String, Macro> getMacros() {
        return Collections.unmodifiableMap(macros);
    }

    public void setMacros(Map<String, Macro> macros) {
        this.macros = macros;
    }

    public Optional<Macro> getMacro(String name) {
        if (macros.containsKey(name.toLowerCase())) {
            return Optional.of(macros.get(name.toLowerCase()));
        } else return Optional.empty();
    }

    public boolean hasMacro(String name) {
        return macros.containsKey(name.toLowerCase());
    }

    public boolean addMacro(Macro macro) {
        if (!macros.containsKey(macro.getId().toLowerCase())) {
            macros.put(macro.getId().toLowerCase(), macro);
            return true;
        } else return false;
    }

    public boolean removeMacro(Macro macro) {
        if (macros.containsKey(macro.getId().toLowerCase())) {
            macros.remove(macro.getId().toLowerCase());
            return true;
        } else return false;
    }

    public boolean removeMacro(String name) {
        if (getMacro(name).isPresent()) {
            return removeMacro(getMacro(name).get());
        } else return false;
    }


}