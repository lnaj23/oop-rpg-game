package models.Bugemon;

import com.google.gson.Gson;
import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class BugemonProvider {

    private static class JsonBugemons {
        public List<Bugemon> bugemons;
    }

    private static class JsonAttack {
        public List<Attack> attaques;
    }

    private static List<Bugemon> bugemonRepo;
    private static List<Attack> attackRepo;

    static {
        readJsonFiles();
    }

    private static void readJsonFiles() {
        Gson gson = new Gson();

        InputStream bugemonStream = BugemonProvider.class.getResourceAsStream("/Assets/json/bugemons.json");
        InputStream attackStream  = BugemonProvider.class.getResourceAsStream("/Assets/json/attaques.json");

        if (bugemonStream == null) throw new IllegalStateException("Resource bugemons.json not found");
        if (attackStream == null)  throw new IllegalStateException("Resource attaques.json not found");

        attackRepo  = gson.fromJson(new InputStreamReader(attackStream),  JsonAttack.class).attaques;
        bugemonRepo = gson.fromJson(new InputStreamReader(bugemonStream), JsonBugemons.class).bugemons;
    }

    static List<Bugemon> getListBugemon(List<String> teamId) {
        List<Bugemon> bugemonList = new ArrayList<>();
        for (String id : teamId) {
            bugemonList.add(getBugemon(id));
        }
        return bugemonList;
    }

    private static Bugemon getBugemon(String id) {
        for (Bugemon bug : bugemonRepo) {
            if (bug.getId().equals(id)) {
                assignAttack(bug);
                return bug;
            }
        }
        throw new BugemonNotFoundException(id);
    }

    private static void assignAttack(Bugemon bug) {
        if (!bug.getAttacks().isEmpty()) return;
        for (String attackName : bug.getAttackNames()) {
            bug.addAttacks(getAttacksFromJson(attackName));
        }
    }

    private static Attack getAttacksFromJson(String id) {
        for (Attack attack : attackRepo) {
            if (attack.getId().equals(id)) return attack;
        }
        throw new AttackNotFoundException(id);
    }

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

    public static String getPv(String id)         { return String.valueOf(getBugemon(id).getStats().getPv()); }
    public static String getAttaque(String id)    { return String.valueOf(getBugemon(id).getStats().getAttaque()); }
    public static String getDefense(String id)    { return String.valueOf(getBugemon(id).getStats().getDefense()); }
    public static String getInitiative(String id) { return String.valueOf(getBugemon(id).getStats().getInitiative()); }

    public static String getAttackNames(String id) {
        return String.join(", ", getBugemon(id).getAttackNames());
    }

    public static List<String> getAttackNamesList(String id) {
        return getBugemon(id).getAttackNames();
    }

    public static List<Bugemon> getAllBugemons() {
        return new ArrayList<>(bugemonRepo);
    }
}