package me.schooltests.chatmacro.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.schooltests.chatmacro.cache.Macro;
import me.schooltests.chatmacro.cache.MacroPlayer;
import me.schooltests.chatmacro.exceptions.NoSuchMacroPlayerException;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class SQLHandler implements StorageHandler {
    enum SQLType { SQLite, MySQL }

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private SQLType type;
    private String address;
    private String database;
    private String username;
    private String password;

    public SQLHandler() {
        this.type = SQLType.SQLite;
    }


    public SQLHandler(String address, String database, String username, String password) {
        this.type = SQLType.MySQL;
        this.address = address;
        this.database = database;
        this.username = username;
        this.password = password;
    }


    private Connection getNewConnection() {
        try {
            if (type == SQLType.SQLite)
                return DriverManager.getConnection("jdbc:sqlite:plugins/ChatMacros/database.db");
            else if (type == SQLType.MySQL)
                return DriverManager.getConnection("jdbc:mysql://" + address + "/" + database, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void setup() {
        try {
            Connection c = Objects.requireNonNull(getNewConnection());
            PreparedStatement statement = c.prepareStatement("CREATE TABLE IF NOT EXISTS macros (ID VARCHAR(36) NOT NULL, OWNER VARCHAR(36) NOT NULL, MACRO_NAME VARCHAR(256) NOT NULL, MACRO TEXT NOT NULL, PRIMARY KEY (ID));");
            statement.executeUpdate();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(MacroPlayer macroPlayer) {
        try {
            Connection c = Objects.requireNonNull(getNewConnection());
            PreparedStatement statement = c.prepareStatement("REPLACE INTO macros (ID, OWNER, MACRO_NAME, MACRO) VALUES (?, ?, ?, ?);");
            for (Macro macro : macroPlayer.getMacros().values()) {
                statement.setString(1, macro.getUniqueID().toString());
                statement.setString(2, macro.getOwner().toString());
                statement.setString(3, macro.getName());
                statement.setString(4, gson.toJson(macro.getMacroSteps()));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MacroPlayer get(UUID user) throws NoSuchMacroPlayerException {
        try {
            Connection c = Objects.requireNonNull(getNewConnection());
            PreparedStatement statement = c.prepareStatement("SELECT * FROM macros WHERE owner=?");
            statement.setString(1, user.toString());
            ResultSet statementResult = statement.executeQuery();
            MacroPlayer data = new MacroPlayer(user, new HashMap<>());
            while (statementResult.next()) {
                Type type = TypeToken.getParameterized(ArrayList.class, String.class).getType();
                Macro macro = new Macro(user, statementResult.getString("MACRO_NAME"), gson.fromJson(statementResult.getString("MACRO"), type), UUID.fromString(statementResult.getString("ID")));
                data.addMacro(macro);
            }

            return data;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new NoSuchMacroPlayerException();
    }
}