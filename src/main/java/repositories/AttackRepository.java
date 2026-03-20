package repositories;

import models.bugemon.Attack;
import java.util.List;

/**
 * Repository for Attack data loaded from {@code attaques.json}.
 * Handles raw deserialization and acts as the single source of truth for Attack data.
 * <p>
 * This class is not intended to be instantiated.
 * Data is loaded once at class loading time via a static initializer.
 * </p>
 */
public abstract class AttackRepository extends JsonRepository {

    /**
     * Mirrors the structure of {@code attaques.json}, which contains a list of
     * {@link Attack} objects under the key {@code "attaques"}.
     * Used exclusively by Gson to deserialize the JSON array.
     */
    public static class JsonAttack implements JsonWrapper<Attack> {
        public List<Attack> attaques;

        @Override
        public List<Attack> getItems() { return attaques; }
    }

    /** Classpath-relative path to the Attack JSON file. */
    private static final String PATH = JSON_PREFIX + "attaques.json";

    /** All Attacks loaded from {@code attaques.json} at startup. */
    private static final List<Attack> attackRepo;

    static {
        attackRepo = readJSON(PATH, JsonAttack.class);
    }

    /**
     * Returns the raw list of all Attacks loaded from {@code attaques.json}.
     *
     * @return the list of all Attacks
     */
    public static List<Attack> getAllAttacks() {
        return attackRepo;
    }
}