package exceptions;

/**
 * Thrown when user-added Bugémons cannot be loaded from disk.
 */
public class LoadingAddedBugemonFailedException extends Exception {
    public LoadingAddedBugemonFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
