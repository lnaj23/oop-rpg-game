package services.models.dataprovider;

import exceptions.JsonResourceNotFoundException;
import models.game.Item;
import repositories.ItemRepository;

import java.util.*;
import java.util.List;
import java.util.Map;

public class ItemProvider {

    private List<Item> getAllItems() throws JsonResourceNotFoundException {
        return ItemRepository.getInstance().getAllItems();
    }

    Map<String, Integer> defaultInventory = new HashMap<>();{
        defaultInventory.put("Baie Revigorante", 3);
        defaultInventory.put("Baie Tonique", 2);
        defaultInventory.put("Gel Défensif", 1);
        defaultInventory.put("Sérum Offensif", 1);
    }

    public List<Item> getDefautItems() throws JsonResourceNotFoundException {
        List<Item> allItems = getAllItems();
        List<Item> defautItems = new ArrayList<>();
        for (Item item : allItems) {
            if (defaultInventory.containsKey(item.getName())) {
                int count = defaultInventory.get(item.getName());
                for (int i = 0; i < count; i++) {
                    defautItems.add(item);
                }
            }
        }
        defautItems.add(new Item("pierre_de_retraite", "Pierre de Retraite", "changer sans perdre l'initiative", null, null, null));
        return defautItems;
    }
    public Item getRandomItem() throws JsonResourceNotFoundException {
        List<Item> items = getAllItems();
        return items.get(new Random().nextInt(items.size()));
    }
}
