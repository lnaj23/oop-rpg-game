package services.dataprovider;

import exceptions.AttackNotFoundException;
import models.bugemon.Attack;
import repositories.AttackRepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to Attack data.
 * Acts as the single entry point for retrieving attacks by ID or in bulk.
 * This class is not intended to be instantiated.
 */
public abstract class AttackProvider {

    /**
     * Searches for an Attack matching the given id.
     *
     * @param id the id of the Attack to retrieve
     * @return the matching Attack
     * @throws AttackNotFoundException if no Attack is found with the given id
     */
    public static Attack getAttackById(String id) {
        for (Attack attack : AttackRepository.getAllAttacks()) {
            if (attack.getId().equals(id)) return new Attack(attack);
        }
        throw new AttackNotFoundException(id);
    }

    /**
     * Returns a copy of all available Attacks.
     *
     * @return the list of all Attacks
     */
    public static List<Attack> getAllAttacks() {
        return new ArrayList<>(AttackRepository.getAllAttacks());
    }
}