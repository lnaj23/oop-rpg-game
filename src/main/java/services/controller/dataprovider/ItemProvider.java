package services.controller.dataprovider;

import exceptions.JsonResourceNotFoundException;
import models.game.Item;
import repositories.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ItemProvider {

    private List<Item> getAllItems() throws JsonResourceNotFoundException {
        return ItemRepository.getInstance().getAllItems();
    }

    public List<Item> getDefautItems() throws JsonResourceNotFoundException {
        List<Item> allItems = getAllItems();
        Map<String, Integer> defaultInventory = ItemRepository.getInstance().getDefaultInventory();

        List<Item> defautItems = new ArrayList<>();

        for (Item item : allItems) {
            if (defaultInventory.containsKey(item.getId())) {
                int count = defaultInventory.get(item.getId());

                for (int i = 0; i < count; i++) {
                    defautItems.add(item);
                }
            }
        }

        return defautItems;
    }

    public Item getRandomItem() throws JsonResourceNotFoundException {
        List<Item> items = getAllItems();
        return items.get(new Random().nextInt(items.size()));
    }
}