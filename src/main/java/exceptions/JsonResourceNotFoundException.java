package exceptions;

public class JsonResourceNotFoundException extends Exception {
    public JsonResourceNotFoundException(String path) {
        super("Resource not found: " + path);
    }
}