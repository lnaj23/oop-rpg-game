package repositories;

import models.bugemon.Bugemon;
import java.util.List;

/**
 * Repository for Bugémon data loaded from bugemons.json.
 * Handles raw deserialization and acts as the single source of truth for Bugémon data.
 * This class is not intended to be instantiated.
 */
public abstract class BugemonRepository extends JsonRepository {

    /**
     * Mirrors the structure of bugemons.json, which contains a list of Bugémon objects
     * under the key "bugemons". Used exclusively by Gson to deserialize the JSON array.
     */
    private static class JsonBugemons {
        public List<Bugemon> bugemons;
    }

    /** All Bugémons loaded from bugemons.json at startup. */
    private static final List<Bugemon> bugemonRepo;

    /** JSON path */
    private static final String PATH = JSON_PREFIX + "bugemons.json";

    /**
     * This class is not intended to be instantiated, so it is declared abstract.
     * So no constructor is used to run initialization code.
     *
     * The initialization is performed in a static block, which executes
     * when the class is loaded.
     */
    static {
        bugemonRepo = readJSON(PATH, JsonBugemons.class, w -> w.bugemons);
    }

    /**
     * Returns the raw list of all Bugémons loaded from bugemons.json.
     *
     * @return the list of all Bugémons
     */
    public static List<Bugemon> getAllBugemons() {
        return bugemonRepo;
    }
}