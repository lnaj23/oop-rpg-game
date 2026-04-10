package repositories;

import models.game.Item;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class ItemRepositoryTest {

    @Test
    public void testGetInstanceNotNull() {
        assertNotNull(ItemRepository.getInstance());
    }

    @Test
    public void testSingletonReturnsSameInstance() {
        assertSame(ItemRepository.getInstance(), ItemRepository.getInstance());
    }

    @Test
    public void testGetAllItemsNotNull() {
        assertNotNull(ItemRepository.getInstance().getAllItems());
    }

    @Test
    public void testGetAllItemsNotEmpty() {
        assertFalse(ItemRepository.getInstance().getAllItems().isEmpty());
    }

    @Test
    public void testGetAllItemsNoNullElement() {
        for (Item item : ItemRepository.getInstance().getAllItems()) {
            assertNotNull(item);
        }
    }

    @Test
    public void testEachItemHasNonNullId() {
        for (Item item : ItemRepository.getInstance().getAllItems()) {
            assertNotNull(item.getId());
            assertFalse(item.getId().isEmpty());
        }
    }

    @Test
    public void testEachItemHasNonNullName() {
        for (Item item : ItemRepository.getInstance().getAllItems()) {
            assertNotNull(item.getName());
        }
    }
}