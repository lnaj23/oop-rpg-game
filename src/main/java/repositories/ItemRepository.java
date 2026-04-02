package repositories;

import com.google.gson.annotations.SerializedName;
import configuration.AppConfig;
import models.bugemon.Bugemon;
import models.game.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository extends JsonRepository {

    private static class JsonItem implements JsonWrapper<Item> {
        @SerializedName("objets")
        public List<Item> items;

        @Override
        public List<Item> getItems() { return items; }
    }
    private static final List<Item> itemRepo;

    static {
        itemRepo = readJSON(AppConfig.JSON_ITEMS, ItemRepository.JsonItem.class);
    }

    public static List<Item> getAllItems() { return itemRepo; }
}
