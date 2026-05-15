package services.controller.dataprovider;

import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import models.game.Item;
import org.junit.Before;
import org.junit.Test;
import repositories.ItemRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemProviderTest extends TestCase {

    private ItemProvider provider;
    private Map<String, Integer> expectedInventory;

    @Before
    public void setUp() throws JsonResourceNotFoundException {
        provider = new ItemProvider();
        expectedInventory = ItemRepository.getInstance().getDefaultInventory();
    }

    @Test
    public void testGetDefautItemsReturnsTotalCountFromInventory() throws JsonResourceNotFoundException {
        int expectedTotal = expectedInventory.values().stream().mapToInt(Integer::intValue).sum();
        List<Item> items = provider.getDefautItems();
        assertEquals(expectedTotal, items.size());
    }

    @Test
    public void testGetDefautItemsDuplicatesItemsAccordingToCount() throws JsonResourceNotFoundException {
        List<Item> items = provider.getDefautItems();
        Map<String, Integer> actualCounts = new HashMap<>();
        for (Item item : items) {
            actualCounts.merge(item.getId(), 1, Integer::sum);
        }
        for (Map.Entry<String, Integer> entry : expectedInventory.entrySet()) {
            assertEquals("wrong count for " + entry.getKey(),
                    entry.getValue(), actualCounts.get(entry.getKey()));
        }
    }

    @Test
    public void testGetDefautItemsOnlyContainsItemsFromInventory() throws JsonResourceNotFoundException {
        List<Item> items = provider.getDefautItems();
        for (Item item : items) {
            assertTrue("unexpected item: " + item.getId(),
                    expectedInventory.containsKey(item.getId()));
        }
    }

    @Test
    public void testGetDefautItemsReturnsNonNullItems() throws JsonResourceNotFoundException {
        List<Item> items = provider.getDefautItems();
        for (Item item : items) {
            assertNotNull(item);
            assertNotNull(item.getId());
        }
    }
}