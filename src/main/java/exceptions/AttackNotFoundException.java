package exceptions;

public class AttackNotFoundException extends RuntimeException {
    public AttackNotFoundException(String id) {
        super("Attack not found: " + id);
    }
}