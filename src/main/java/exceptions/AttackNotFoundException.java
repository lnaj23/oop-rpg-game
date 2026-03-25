package exceptions;

/**
 * Thrown when an attack cannot be found by its ID.
 */
public class AttackNotFoundException extends Exception {

    /**
     * @param id the ID of the attack that was not found
     */
    public AttackNotFoundException(String id) {
        super("Attack not found: " + id);
    }
}