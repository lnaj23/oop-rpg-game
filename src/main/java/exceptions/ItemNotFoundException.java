package exceptions;

/**
 * Thrown when an item cannot be found by its ID.
 */
public class ItemNotFoundException extends Exception {

    /**
     * @param id the ID of the item that was not found
     */
    public ItemNotFoundException(String id) {
        super("Item not found: " + id);
    }
}