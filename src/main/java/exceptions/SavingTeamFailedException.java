package exceptions;

/**
 * Thrown when saving teams to disk fails.
 */
public class SavingTeamFailedException extends Exception {
    public SavingTeamFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}