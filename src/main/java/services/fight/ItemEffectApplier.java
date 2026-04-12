package services.fight;

import models.bugemon.Bugemon;
import models.game.Item;

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
