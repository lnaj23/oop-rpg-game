package exceptions;

/**
 * Thrown when a saved team cannot be loaded from storage.
 */
public class LoadingTeamFailedException extends Exception {
    public LoadingTeamFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}