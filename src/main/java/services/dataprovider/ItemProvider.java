package services.dataprovider;

import configuration.AppConfig;
import models.game.Item;
import repositories.ItemRepository;

import java.util.*;
import java.util.List;
import java.util.Map;

public class ItemProvider {
    private static ItemProvider instance;

    public static ItemProvider getInstance() {
        if (instance == null) {
            instance = new ItemProvider();
        }
        return instance;
    }

    public List<Item> getAllItems() {
        return ItemRepository.getAllItems();
    }

    Map<String, Integer> defaultIventory = new HashMap<>();{
    defaultIventory.put("Baie Revigorante", 3);
    defaultIventory.put("Baie Tonique", 2);
    defaultIventory.put("Gel Défensif", 1);
    defaultIventory.put("Sérum Offensif", 1);
    }

    public List<Item> getDefautItems() {
        List<Item> allItems = getAllItems();
        List<Item> defautItems = new ArrayList<>();
        for(Item item : allItems) {
            if(defaultIventory.containsKey(item.getName())) {
                int count = defaultIventory.get(item.getName());
                for(int i = 0; i < count; i++) {
                    defautItems.add(item);
                }
            }
        }
        defautItems.add(new Item("pierre_de_retraite", "Pierre de Retraite", "changer sans perdre l'initiative",null,null,null));
        return defautItems;
    }
}
