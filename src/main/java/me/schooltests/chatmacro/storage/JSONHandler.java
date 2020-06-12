package me.schooltests.chatmacro.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.schooltests.chatmacro.ChatMacro;
import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class JSONHandler implements StorageHandler {
    private ChatMacro plugin;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private File macroFolder;
    public JSONHandler(ChatMacro plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setup() {
        macroFolder = new File(plugin.getDataFolder(), "macros");
        final boolean success = macroFolder.mkdirs();
        if (!success) plugin.debug("Failing to creating JSON data files!");
    }

    @Override
    public void put(MacroPlayer macroPlayer) {
        try {
            File file = new File(macroFolder, macroPlayer.getUuid() + ".json");
            FileWriter fileWriter = new FileWriter(file);
            gson.toJson(macroPlayer, fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MacroPlayer get(UUID user) throws NoSuchMacroPlayerException {
        File file = new File(macroFolder, user.toString() + ".json");
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                MacroPlayer data = gson.fromJson(fileReader, MacroPlayer.class);
                return data;
            } catch (IOException e) {
                throw new NoSuchMacroPlayerException();
            }
        } else {
            throw new NoSuchMacroPlayerException();
        }
    }
}
