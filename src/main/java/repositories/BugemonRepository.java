package repositories;

import configuration.AppConfig;
import models.bugemon.Bugemon;
import java.util.List;

/**
 * Repository for Bugémon data loaded from {@code bugemons.json}.
 * Handles raw deserialization and acts as the single source of truth for Bugémon data.
 * <p>
 * This class is not intended to be instantiated.
 * Data is loaded once at class loading time via a static initializer.
 * </p>
 */
public abstract class BugemonRepository extends JsonRepository {

    /**
     * Mirrors the structure of {@code bugemons.json}, which contains a list of
     * {@link Bugemon} objects under the key {@code "bugemons"}.
     * Used exclusively by Gson to deserialize the JSON array.
     */
    private static class JsonBugemons implements JsonWrapper<Bugemon> {
        public List<Bugemon> bugemons;

        @Override
        public List<Bugemon> getItems() { return bugemons; }
    }

    /** All Bugémons loaded from {@code bugemons.json} at startup. */
    private static final List<Bugemon> bugemonRepo;

    static {
        bugemonRepo = readJSON(AppConfig.JSON_BUGEMONS, JsonBugemons.class);
    }

    /**
     * Returns the raw list of all Bugémons loaded from {@code bugemons.json}.
     *
     * @return the list of all Bugémons
     */
    public static List<Bugemon> getAllBugemons() {
        return bugemonRepo;
    }
}