package services.dataprovider;

import exceptions.AttackNotFoundException;
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

    static {
        try {
            for (Bugemon bug : BugemonRepository.getAllBugemons()) {
                assignAttacks(bug);
            }
            BugemonRepository.getAllBugemons().forEach(Bugemon::initColor);
            BugemonRepository.getAllBugemons().forEach(Bugemon::initPv);
        } catch (AttackNotFoundException e) {
            throw new IllegalStateException("Failed to assign attacks during initialization: " + e.getMessage(), e);
        }
    }

    /**
     * Populates the Attack list of the given Bugémon.
     * Skips assignment if attacks are already loaded to avoid duplicates.
     *
     * @param bug the Bugémon whose attacks need to be assigned
     * @throws AttackNotFoundException if an attack ID cannot be resolved
     */
    private static void assignAttacks(Bugemon bug) throws AttackNotFoundException {
        if (!bug.getAttacks().isEmpty()) return;
        for (String id : bug.getAttackNamesAsList()) {
            bug.addAttacks(AttackProvider.getAttackById(id));
        }
    }

    /**
     * Returns a list of Bugémon objects matching the given IDs.
     *
     * @param teamIds list of Bugémon IDs to retrieve
     * @return the corresponding list of Bugémons
     * @throws BugemonNotFoundException if any ID does not match a known Bugémon
     */
    public static List<Bugemon> getListBugemon(List<String> teamIds) throws BugemonNotFoundException {
        List<Bugemon> list = new ArrayList<>();
        for (String id : teamIds) {
            list.add(getBugemon(id));
        }
        return list;
    }

    /**
     * Searches for a Bugémon matching the given id.
     *
     * @param id the id of the Bugémon to retrieve
     * @return the matching Bugémon
     * @throws BugemonNotFoundException if no Bugémon is found with the given id
     */
    private static Bugemon getBugemon(String id) throws BugemonNotFoundException {
        for (Bugemon bug : BugemonRepository.getAllBugemons()) {
            if (bug.getId().equals(id)) return new Bugemon(bug);
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