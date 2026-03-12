package services.bugemon;

import exceptions.BugemonNotFoundException;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import repositories.BugemonRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to Bugémon data with their attacks pre-assigned.
 * Acts as the single entry point for retrieving Bugémons by ID or in bulk.
 * This class is not intended to be instantiated.
 */
public abstract class BugemonProvider {

    /**
     * Assigns attacks to all Bugémons at startup.
     * Uses {@link AttackProvider} to resolve each attack by ID.
     */
    static {
        BugemonRepository.getAllBugemons().forEach(BugemonProvider::assignAttacks);
    }

    /**
     * Populates the Attack list of the given Bugémon.
     * Skips assignment if attacks are already loaded to avoid duplicates.
     *
     * @param bug the Bugémon whose attacks need to be assigned
     */
    private static void assignAttacks(Bugemon bug) {
        if (!bug.getAttacks().isEmpty()) return;
        bug.getAttackNamesAsList().forEach(id -> bug.addAttacks(AttackProvider.getAttackById(id)));
    }

    /**
     * Returns a list of Bugémon objects matching the given IDs.
     *
     * @param teamIds list of Bugémon IDs to retrieve
     * @return the corresponding list of Bugémons
     * @throws BugemonNotFoundException if any ID does not match a known Bugémon
     */
    public static List<Bugemon> getListBugemon(List<String> teamIds) {
        List<Bugemon> list = new ArrayList<>();
        teamIds.forEach(id -> list.add(getBugemon(id)));
        return list;
    }

    /**
     * Searches for a Bugémon matching the given id.
     *
     * @param id the id of the Bugémon to retrieve
     * @return the matching Bugémon
     * @throws BugemonNotFoundException if no Bugémon is found with the given id
     */
    private static Bugemon getBugemon(String id) {
        for (Bugemon bug : BugemonRepository.getAllBugemons()) {
            if (bug.getId().equals(id)) return bug;
        }
        throw new BugemonNotFoundException(id);
    }

    /**
     * Returns a copy of all available Bugémons with their attacks assigned.
     *
     * @return the list of all Bugémons
     */
    public static List<Bugemon> getAllBugemons() {
        return new ArrayList<>(BugemonRepository.getAllBugemons());
    }

    /**
     * Returns a copy of all available Bugémons as DTOs.
     *
     * @return the list of all Bugémons as {@link BugemonDTO}
     */
    public static List<BugemonDTO> getAllBugemonsDTO() {
        return new ArrayList<>(BugemonRepository.getAllBugemons());
    }
}