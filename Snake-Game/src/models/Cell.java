package models;

import java.util.Optional;
import java.util.UUID;

public class Cell {
    String id;
    Optional<Jumper> jumper;

    public Cell() {
        this.id = UUID.randomUUID().toString();
    }

    public void setJumper(Jumper jumper) {
        this.jumper = Optional.of(jumper);
    }

    public Optional<Jumper> getJumper() {
        return this.jumper;
    }
}
