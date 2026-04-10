package repositories;

import exceptions.JsonResourceNotFoundException;
import models.game.Item;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class ItemRepositoryTest {

    @Test
    public void testGetInstanceNotNull() throws JsonResourceNotFoundException {
        assertNotNull(ItemRepository.getInstance());
    }

    @Test
    public void testSingletonReturnsSameInstance() throws JsonResourceNotFoundException {
        assertSame(ItemRepository.getInstance(), ItemRepository.getInstance());
    }

    @Test
    public void testGetAllItemsNotNull() throws JsonResourceNotFoundException {
        assertNotNull(ItemRepository.getInstance().getAllItems());
    }

    @Test
    public void testGetAllItemsNotEmpty() throws JsonResourceNotFoundException {
        assertFalse(ItemRepository.getInstance().getAllItems().isEmpty());
    }

    @Test
    public void testGetAllItemsNoNullElement() throws JsonResourceNotFoundException {
        for (Item item : ItemRepository.getInstance().getAllItems()) {
            assertNotNull(item);
        }
    }

    @Test
    public void testEachItemHasNonNullId() throws JsonResourceNotFoundException {
        for (Item item : ItemRepository.getInstance().getAllItems()) {
            assertNotNull(item.getId());
            assertFalse(item.getId().isEmpty());
        }
    }

    @Test
    public void testEachItemHasNonNullName() throws JsonResourceNotFoundException {
        for (Item item : ItemRepository.getInstance().getAllItems()) {
            assertNotNull(item.getName());
        }
    }
}