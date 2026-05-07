package exceptions;

/**
 * Thrown when writing mutable persistence data to the filesystem fails.
 */
public class JsonPersistenceSavingException extends Exception {
    public JsonPersistenceSavingException(String message, Throwable cause) {
        super(message, cause);
    }
}