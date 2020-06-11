package me.schooltests.chatmacro.storage;

import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;

import java.util.UUID;

public class JSONHandler implements StorageHandler {
    @Override
    public void setup() {

    }

    @Override
    public void put(MacroPlayer macroPlayer) {

    }

    @Override
    public MacroPlayer get(UUID user) throws NoSuchMacroPlayerException {
        return null;
    }
}
