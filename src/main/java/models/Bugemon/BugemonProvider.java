package models.Bugemon;

import com.google.gson.Gson;
import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*
 * Provides access to Bugemon and Attack data loaded from JSON files.
 * Acts as the single entry point for retrieving Bugemon stats and building teams.
 */
public abstract class BugemonProvider {

    /*
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

    private static List<Bugemon> bugemonRepo;
    private static List<Attack> attackRepo;

    /*
     * This class is not intended to be instantiated, so it is declared abstract.
     * So no constructor is used to run initialization code.
     *
     * The initialization is performed in a static block, which executes
     * when the class is loaded.
     */
    static {
        readJsonFiles();
    }

    /*
     * Reads bugemons.json and attaques.json from resources and populates
     * bugemonRepo and AttackRepo. Throws IllegalStateException if a file is missing.
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

    /*
     * Returns a List of Bugemon objects, used by the TeamBugemon constructor.
     * The next three methods are only used by this method.
     * Throws BugemonNotFoundException if any id in the list does not match a known Bugemon.
     *
     * @param teamId List of Strings representing the ids of each Bugemon in the team
     * @return List of Bugemon objects
     * @throws BugemonNotFoundException if any id is not found
     */
    static List<Bugemon> getListBugemon(List<String> teamId) {
        List<Bugemon> bugemonList = new ArrayList<>();
        for (String id : teamId) {
            bugemonList.add(getBugemon(id));
        }
        return bugemonList;
    }

    /*
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
                assignAttack(bug);
                return bug;
            }
        }
        throw new BugemonNotFoundException(id);
    }

    /*
     * Populates the Attack list of the given Bugemon from AttackRepo.
     * Skips assignment if attacks are already loaded to avoid duplicates.
     *
     * @param bug the Bugemon whose attacks need to be assigned
     */
    private static void assignAttack(Bugemon bug) {
        if (!bug.getAttacks().isEmpty()) return;
        for (String attackName : bug.getAttackNames()) {
            bug.addAttacks(getAttacksFromJson(attackName));
        }
    }

    /*
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

    /*
     * Returns the requested stat of the Bugemon with the given id as a String.
     * Accepted values for statsToGet: "pv", "attaque", "defense", "initiative", "attaques".
     * Returns null if the Bugemon is not found or if the stat name is unknown.
     *
     * This method is intended to be used in the team selection screen, where stats
     * need to be displayed on hover before any TeamBugemon object is created.
     *
     * @param id         the id of the Bugemon
     * @param statsToGet the name of the stat to retrieve
     * @return the requested stat as a String, or null if not found
     */
    public static String getSTATS(String id, String statsToGet) {
        switch (statsToGet.toLowerCase()) {
            case "pv":         return getPv(id);
            case "attaque":    return getAttaque(id);
            case "defense":    return getDefense(id);
            case "initiative": return getInitiative(id);
            case "attaques":   return getAttackNames(id);
            default:
                System.err.println("Unknown stat: " + statsToGet);
                return null;
        }
    }

    /*
     * Returns the max HP of the Bugemon with the given id as a String.
     *
     * @param id the id of the Bugemon
     * @return max HP as a String, or null if not found
     */
    public static String getPv(String id) {
        Bugemon bug = getBugemon(id);
        return String.valueOf(bug.getStats().getPv());
    }

    /*
     * Returns the attack stat of the Bugemon with the given id as a String.
     *
     * @param id the id of the Bugemon
     * @return attack stat as a String, or null if not found
     */
    public static String getAttaque(String id) {
        Bugemon bug = getBugemon(id);
        return String.valueOf(bug.getStats().getAttaque());
    }

    /*
     * Returns the defense stat of the Bugemon with the given id as a String.
     *
     * @param id the id of the Bugemon
     * @return defense stat as a String, or null if not found
     */
    public static String getDefense(String id) {
        Bugemon bug = getBugemon(id);
        return String.valueOf(bug.getStats().getDefense());
    }

    /*
     * Returns the initiative stat of the Bugemon with the given id as a String.
     *
     * @param id the id of the Bugemon
     * @return initiative stat as a String, or null if not found
     */
    public static String getInitiative(String id) {
        Bugemon bug = getBugemon(id);
        return String.valueOf(bug.getStats().getInitiative());
    }

    /*
     * Returns the names of the attacks of the Bugemon with the given id as a String.
     *
     * @param id the id of the Bugemon
     * @return attack names separated by ", " as a String, or null if not found
     */
    public static String getAttackNames(String id) {
        Bugemon bug = getBugemon(id);
        return String.join(", ", bug.getAttackNames());
    }

    /*
     * Returns the names of the attacks of the Bugemon with the given id as a List of Strings.
     *
     * @param id the id of the Bugemon
     * @return attack names as a List of Strings, or null if not found
     */
    public static List<String> getAttackNamesList(String id) {
        Bugemon bug = getBugemon(id);
        return bug.getAttackNames();
    }

    public static List<Bugemon> getAllBugemons() {
        return new ArrayList<>(bugemonRepo);
    }

}