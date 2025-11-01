package org.example.enums;

public enum PlayerSymbol {
    X('X'),
    O('O');

    private final char c;

    PlayerSymbol(char c) {
        this.c = c;
    }


    public char getChar() {
        return c;
    }
}
