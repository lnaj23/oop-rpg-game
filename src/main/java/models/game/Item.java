package models.game;

import models.bugemon.dto.ItemDTO;

/**
 * Represents an item that can be used during a fight.
 */
public class Item implements ItemDTO {
    private final String id;
    private final String name;
    private final String description;

    public Item(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return name + " : " + description;
    }
}