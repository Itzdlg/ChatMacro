package me.schooltests.chatmacro.storage;

import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;

import java.util.UUID;

public interface StorageHandler {
    void setup();

    void put(MacroPlayer macroPlayer);

    MacroPlayer get(UUID user) throws NoSuchMacroPlayerException;
}
