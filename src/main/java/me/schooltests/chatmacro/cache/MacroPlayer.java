package me.schooltests.chatmacro.cache;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
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
        if (!macros.containsKey(macro.getName().toLowerCase())) {
            macros.put(macro.getName().toLowerCase(), macro);
            return true;
        } else return false;
    }

    private boolean removeMacro(Macro macro) {
        if (macros.containsKey(macro.getName().toLowerCase())) {
            macros.remove(macro.getName().toLowerCase());
            return true;
        } else return false;
    }

    public boolean removeMacro(String name) {
        if (getMacro(name).isPresent()) {
            return removeMacro(getMacro(name).get());
        } else return false;
    }


}