package exceptions;

/**
 * Thrown when reading mutable persistence data from the filesystem fails
 * (I/O error or malformed JSON). Not thrown when the file is simply absent —
 * in that case an empty list is returned.
 */
public class JsonPersistenceLoadingException extends Exception {
    public JsonPersistenceLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}