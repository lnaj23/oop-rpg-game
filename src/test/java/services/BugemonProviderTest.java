package services;

import exceptions.BugemonNotFoundException;
import junit.framework.TestCase;
import models.bugemon.Attack;
import models.bugemon.Bugemon;
import org.junit.Test;
import services.dataprovider.BugemonProvider;

import java.util.Arrays;
import java.util.List;

public class BugemonProviderTest extends TestCase {
    @Test
    public void testGetListBugemonReturnsNotNull() {
        List<Bugemon> list = BugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        assertNotNull(list);
    }

    @Test
    public void testGetListBugemonCorrectSize() {
        List<Bugemon> list = BugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        assertEquals(3, list.size());
    }

    @Test
    public void testGetListBugemonEmptyListReturnsEmpty() {
        List<Bugemon> list = BugemonProvider.getListBugemon(Arrays.asList());
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testGetListBugemonUnknownIdThrows() {
        try {
            BugemonProvider.getListBugemon(Arrays.asList("unknown_id"));
            fail("Expected BugemonNotFoundException");
        } catch (BugemonNotFoundException e) {
            assertTrue(e.getMessage().contains("unknown_id"));
        }
    }

    @Test
    public void testGetListBugemonMixedIdsThrows() {
        try {
            BugemonProvider.getListBugemon(Arrays.asList("florachu", "unknown_id", "exceflam"));
            fail("Expected BugemonNotFoundException");
        } catch (BugemonNotFoundException e) {
            assertTrue(e.getMessage().contains("unknown_id"));
        }
    }

    @Test
    public void testGetListBugemonCorrectIds() {
        List<Bugemon> list = BugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam"));
        assertEquals("florachu", list.get(0).getId());
        assertEquals("exceflam", list.get(1).getId());
    }

    @Test
    public void testGetListBugemonNoNullElements() {
        List<Bugemon> list = BugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        for (Bugemon bug : list) {
            assertNotNull(bug);
        }
    }

    @Test
    public void testGetListBugemonAllTypesReturnsCorrectSize() {
        List<Bugemon> list = BugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide", "rockachu"));
        assertEquals(4, list.size());
    }
    @Test
    public void testGetListBugemonHasAttacks() {
        List<Bugemon> list = BugemonProvider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        for (Bugemon bug : list) {
            assertNotNull(bug.getAttacks());
            assertFalse("Attacks should not be empty for " + bug.getId(), bug.getAttacks().isEmpty());
        }
    }

    @Test
    public void testGetListBugemonAttacksNotNull() {
        List<Bugemon> list = BugemonProvider.getListBugemon(Arrays.asList("florachu"));
        for (Attack attack : list.get(0).getAttacks()) {
            assertNotNull(attack);
        }
    }

    @Test
    public void testGetAllBugemonsHaveAttacks() {
        List<Bugemon> list = BugemonProvider.getAllBugemons();
        for (Bugemon bug : list) {
            assertFalse("Attacks should not be empty for " + bug.getId(), bug.getAttacks().isEmpty());
        }
    }
}