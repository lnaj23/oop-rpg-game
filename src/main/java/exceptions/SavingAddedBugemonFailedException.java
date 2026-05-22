package exceptions;

/**
 * Thrown when a user-added Bugémon cannot be saved to disk.
 */
public class SavingAddedBugemonFailedException extends Exception {
    public SavingAddedBugemonFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
