package exceptions;

public class LoadingTeamFailedException extends Exception {
    public LoadingTeamFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}