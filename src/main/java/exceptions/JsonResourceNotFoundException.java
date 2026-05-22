package exceptions;

/**
 * Thrown when a bundled JSON resource cannot be found on the classpath.
 */
public class JsonResourceNotFoundException extends Exception {
    public JsonResourceNotFoundException(String path) {
        super("Resource not found: " + path);
    }
}