package exceptions;

/**
 * Thrown when a Bugémon cannot be found by its ID.
 */
public class BugemonNotFoundException extends Exception {

    /**
     * @param id the ID of the Bugémon that was not found
     */
    public BugemonNotFoundException(String id) {
        super("Bugemon not found: " + id);
    }
}