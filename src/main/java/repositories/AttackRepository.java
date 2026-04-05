package repositories;

import com.google.gson.annotations.SerializedName;
import configuration.AppConfig;
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
        @SerializedName("attaques")
        public List<Attack> attacks;

        @Override
        public List<Attack> getItems() { return attacks; }
    }


    /** All Attacks loaded from {@code attaques.json} at startup. */
    private static final List<Attack> attackRepo;

    static {
        attackRepo = readJSON(AppConfig.JSON_ATTAQUES, JsonAttack.class);
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