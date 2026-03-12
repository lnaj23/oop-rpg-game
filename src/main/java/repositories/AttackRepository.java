package repositories;

import models.bugemon.Attack;

import java.util.List;

/**
 * Repository for Attack data loaded from attaques.json.
 * Handles raw deserialization and acts as the single source of truth for Attack data.
 * This class is not intended to be instantiated.
 */
public abstract class AttackRepository extends JsonRepository {

    /**
     * Mirrors the structure of attaques.json, which contains a list of Attack objects
     * under the key "attaques". Used exclusively by Gson to deserialize the JSON array.
     */
    public static class JsonAttack {
        public List<Attack> attaques;
    }

    /** All Attacks loaded from attaques.json at startup. */
    private static final List<Attack> attackRepo;

    /** JSON path */
    private static final String PATH = JSON_PREFIX + "attaques.json";

    /**
     * This class is not intended to be instantiated, so it is declared abstract.
     * So no constructor is used to run initialization code.
     *
     * The initialization is performed in a static block, which executes
     * when the class is loaded.
     */
    static {
        attackRepo = readJSON(PATH, JsonAttack.class, w -> w.attaques);
    }

    /**
     * Returns the raw list of all Attacks loaded from attaques.json.
     *
     * @return the list of all Attacks
     */
    public static List<Attack> getAllAttacks() {
        return attackRepo;
    }
}