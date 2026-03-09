package services;

import com.google.gson.Gson;
import models.Bugemon.DTO.BugemonDTO;
import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;
import models.Bugemon.Attack;
import models.Bugemon.Bugemon;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to Bugemon and Attack data loaded from JSON files.
 * Acts as the single entry point for retrieving Bugemon stats and building teams.
 */
public abstract class BugemonProvider {

    /**
     * These inner classes mirror the structure of the JSON files bugemons.json and attaques.json,
     * which respectively contain a list of Bugemon objects under the key "bugemons"
     * and a list of Attack objects under the key "attaques".
     * They are used exclusively by Gson to deserialize the JSON arrays into Java objects.
     */
    private static class JsonBugemons {
        public List<Bugemon> bugemons;
    }

    private static class JsonAttack {
        public List<Attack> attaques;
    }

    /** All Bugémons loaded from bugemons.json at startup. */
    private static List<Bugemon> bugemonRepo;

    /** All Attacks loaded from attaques.json at startup. */
    private static List<Attack> attackRepo;

    /**
     * This class is not intended to be instantiated, so it is declared abstract.
     * So no constructor is used to run initialization code.
     *
     * The initialization is performed in a static block, which executes
     * when the class is loaded.
     */
    static {
        readJsonFiles();
        //We are making sure that all the bugemo already have their attacks
        bugemonRepo.forEach(bug -> assignAttack(bug));
    }

    /**
     * Reads bugemons.json and attaques.json from resources and populates
     * bugemonRepo and AttackRepo.
     * @throws IllegalStateException if a file is missing.
     */
    private static void readJsonFiles() {
        Gson gson = new Gson();

        InputStream bugemonStream = BugemonProvider.class.getResourceAsStream("/Assets/json/bugemons.json");
        InputStream attackStream  = BugemonProvider.class.getResourceAsStream("/Assets/json/attaques.json");

        if (bugemonStream == null) throw new IllegalStateException("Resource bugemons.json not found");
        if (attackStream == null)  throw new IllegalStateException("Resource attaques.json not found");

        attackRepo  = gson.fromJson(new InputStreamReader(attackStream),  JsonAttack.class).attaques;
        bugemonRepo = gson.fromJson(new InputStreamReader(bugemonStream), JsonBugemons.class).bugemons;
    }

    /**
     * Returns a List of Bugemon objects, used by the TeamBugemon constructor.
     * The next three methods are only used by this method.
     * Throws BugemonNotFoundException if any id in the list does not match a known Bugemon.
     *
     * @param teamId List of Strings representing the ids of each Bugemon in the team
     * @return List of Bugemon objects
     * @throws BugemonNotFoundException if any id is not found
     */
    public static List<Bugemon> getListBugemon(List<String> teamId) {
        List<Bugemon> bugemonList = new ArrayList<>();
        teamId.forEach(id -> bugemonList.add(getBugemon(id)));
        return bugemonList;
    }

    /**
     * Searches bugemonRepo for a Bugemon matching the given id.
     * Assigns its attacks before returning it.
     * Throws BugemonNotFoundException if no Bugemon matches the given id.
     *
     * @param id the id of the Bugemon to retrieve
     * @return the matching Bugemon
     * @throws BugemonNotFoundException if no Bugemon is found with the given id
     */
    private static Bugemon getBugemon(String id) {
        for (Bugemon bug : bugemonRepo) {
            if (bug.getId().equals(id)) {
                return bug;
            }
        }
        throw new BugemonNotFoundException(id);
    }

    /**
     * Populates the Attack list of the given Bugemon from AttackRepo.
     * Skips assignment if attacks are already loaded to avoid duplicates.
     *
     * @param bug the Bugemon whose attacks need to be assigned
     */
    private static void assignAttack(Bugemon bug) {
        if (!bug.getAttacks().isEmpty()) return;
        bug.getAttackNamesAsList().forEach(attackName -> bug.addAttacks(getAttacksFromJson(attackName)));
    }

    /**
     * Searches AttackRepo for an Attack matching the given id.
     * Returns null and logs an error if not found.
     *
     * @param id the id of the Attack to retrieve
     * @return the matching Attack, or null if not found
     */
    private static Attack getAttacksFromJson(String id) {
        for (Attack attack : attackRepo) {
            if (attack.getId().equals(id)) return attack;
        }
        throw new AttackNotFoundException(id);
    }

    public static List<Bugemon> getAllBugemons() {
        return new ArrayList<>(bugemonRepo);
    }
    public static List<BugemonDTO> getAllBugemonsDTO() {
        return new ArrayList<>(bugemonRepo);
    }
}