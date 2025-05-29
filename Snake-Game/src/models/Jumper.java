package models;

import enums.JumperType;

public class Jumper {
    int start;
    int end;
    JumperType type;

    public Jumper(int start, int end, JumperType type) {
        this.start = start;
        this.end = end;
        this.type = type;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public JumperType getType() { return type; }
}
