package exceptions;

public class BugemonNotFoundException extends RuntimeException {
    public BugemonNotFoundException(String id) {
        super("Bugemon not found: " + id);
    }
}