package repositories;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import configuration.AppConfig;
import exceptions.JsonResourceNotFoundException;
import models.game.Item;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Singleton repository loading the items and the default starting inventory
 * from the items JSON resource.
 */
public class ItemRepository extends JsonRepository {

    /**
     * Mirrors the structure of {@code objets.json}: the list of {@link Item}
     * objects under {@code "objets"} and the default starting inventory
     * (item id -> quantity) under {@code "inventaire_depart"}.
     * Used exclusively by Gson to deserialize the file.
     */
    private static class JsonItem implements JsonWrapper<Item> {
        @SerializedName("objets")
        public List<Item> items;

        @SerializedName("inventaire_depart")
        public Map<String, Integer> defaultInventory;

        @Override
        public List<Item> getJsonData() {
            return items;
        }
    }

    private static List<Item> itemRepo = new ArrayList<>();
    private static Map<String, Integer> defaultInventory;
    private static ItemRepository instance;

    private ItemRepository() throws JsonResourceNotFoundException {
        itemRepo = readJSONFromAssets(AppConfig.JSON_ITEMS, JsonItem.class);
        defaultInventory = readDefaultInventoryFromAssets();
    }

    public static ItemRepository getInstance() throws JsonResourceNotFoundException {
        if (instance == null) {
            instance = new ItemRepository();
        }
        return instance;
    }

    public List<Item> getAllItems() {
        return itemRepo;
    }

    public Map<String, Integer> getDefaultInventory() {
        return defaultInventory;
    }

    private Map<String, Integer> readDefaultInventoryFromAssets() throws JsonResourceNotFoundException {
        InputStream stream = JsonRepository.class.getResourceAsStream(AppConfig.JSON_ITEMS);

        if (stream == null) {
            throw new JsonResourceNotFoundException(AppConfig.JSON_ITEMS);
        }

        JsonItem jsonItem = new Gson().fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), JsonItem.class);

        return jsonItem.defaultInventory;
    }
}