package exceptions;

public class TowerNotInitializedException extends Exception {
    public TowerNotInitializedException() {
        super("Current level tower is not set.");
    }
}