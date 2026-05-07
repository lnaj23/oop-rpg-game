package repositories;

import com.google.gson.annotations.SerializedName;
import configuration.AppConfig;
import exceptions.JsonResourceNotFoundException;
import models.game.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository extends JsonRepository {

    private static class JsonItem implements JsonWrapper<Item> {
        @SerializedName("objets")
        public List<Item> items;

        @Override
        public List<Item> getJsonData() { return items; }
    }

    private static List<Item> itemRepo = new ArrayList<>();
    private static ItemRepository instance;

    private ItemRepository() throws JsonResourceNotFoundException {
        itemRepo = readJSONFromAssets(AppConfig.JSON_ITEMS, ItemRepository.JsonItem.class);
    }

    public static ItemRepository getInstance() throws JsonResourceNotFoundException {
        if (instance == null){
            instance = new ItemRepository();
        }
        return instance;
    }

    public List<Item> getAllItems() {
        return itemRepo;
    }
}
