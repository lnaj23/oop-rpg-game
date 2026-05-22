package models.game.dto;

/**
 * Read-only view of an {@link models.game.Item} exposed to the views.
 */
public interface ItemDTO {
    String getId();
    String getName();
    String getDescription();
}
