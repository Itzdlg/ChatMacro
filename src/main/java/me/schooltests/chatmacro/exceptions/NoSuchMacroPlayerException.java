package me.schooltests.chatmacro.exceptions;

public class NoSuchMacroPlayerException extends Exception {
    public NoSuchMacroPlayerException() {
        super("Unable to find suitable MacroPlayer in storage");
    }
}
