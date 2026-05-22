package services.models.fight;

import models.bugemon.Bugemon;
import models.game.Item;

/**
 * Stateless helper applying an item's effect to a Bugémon (healing, stat boost
 * or debuff cancellation) depending on the item's category.
 */
public class ItemEffectApplier {

    public static void ApplyItems(Bugemon bugemon, Item item) {

        switch (item.getCategory()){
            case "soin":
                bugemon.heal(item.getEffectValue());
                break;
            case "boost":
                bugemon.updatestatModifierFromItem(item);
                break;
            case "reset_malus":
                bugemon.cancelDebuff();
                break;
        }
    }
}
