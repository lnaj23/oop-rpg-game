package exceptions;

/**
 * Thrown when an operation requires the current level tower but none has been set.
 */
public class TowerNotInitializedException extends Exception {
    public TowerNotInitializedException() {
        super("Current level tower is not set.");
    }
}