package models.game;

import com.google.gson.annotations.SerializedName;
import models.bugemon.Effect;
import models.game.dto.ItemDTO;

import java.util.List;

/**
 * Represents an item that can be used during a fight.
 */
public class Item implements ItemDTO {
    private final String id;

    @SerializedName("nom")
    private final String name;

    private final String description;

    @SerializedName("categorie")
    private final String category;

    @SerializedName("effet")
    private final Effect effect;

    private final String sprite;

    public Item(String id, String name, String description, String category,Effect effect, String sprite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.effect = effect;
        this.sprite = sprite;
    }




    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public int getEffectValue() {return this.effect.getValeur();}
    public String getStatToModifie(){ return this.effect.getStat();}
    public int getModifier() { return this.effect.getModifier();}
    public Effect getEffect() { return this.effect; }
    @Override
    public String toString() {
        return name + " : " + description;
    }
}