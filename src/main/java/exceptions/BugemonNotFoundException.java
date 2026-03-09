package exceptions;

/**
 * Thrown when a Bugémon cannot be found by its ID.
 */
public class BugemonNotFoundException extends RuntimeException {

    /**
     * @param id the ID of the Bugémon that was not found
     */
    public BugemonNotFoundException(String id) {
        super("Bugemon not found: " + id);
    }
}