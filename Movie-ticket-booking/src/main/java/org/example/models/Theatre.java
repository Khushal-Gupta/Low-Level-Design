package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class Theatre {
    private final String id;
    private final String name;
    private final List<Screen> screens;
    private final Location location;

    public Theatre(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.screens = new ArrayList<>();
        this.location = location;
    }

    public void addScreens(List<Screen> screens) {
        this.screens.addAll(screens);
    }

    public String getId() {
        return id;
    }
}
