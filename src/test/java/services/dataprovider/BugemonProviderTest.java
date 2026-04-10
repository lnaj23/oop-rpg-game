package services.dataprovider;

import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;
import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import models.bugemon.Attack;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BugemonProviderTest extends TestCase {

    private final BugemonProvider provider = new BugemonProvider();

    public BugemonProviderTest() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    @Test
    public void testGetListBugemonReturnsNotNull() throws BugemonNotFoundException, JsonResourceNotFoundException {
        List<Bugemon> list = provider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        assertNotNull(list);
    }

    @Test
    public void testGetListBugemonCorrectSize() throws BugemonNotFoundException, JsonResourceNotFoundException {
        List<Bugemon> list = provider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        assertEquals(3, list.size());
    }

    @Test
    public void testGetListBugemonEmptyListReturnsEmpty() throws BugemonNotFoundException, JsonResourceNotFoundException {
        List<Bugemon> list = provider.getListBugemon(Arrays.asList());
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testGetListBugemonUnknownIdThrows() throws JsonResourceNotFoundException{
        try {
            provider.getListBugemon(Arrays.asList("unknown_id"));
            fail("Expected BugemonNotFoundException");
        } catch (BugemonNotFoundException e) {
            assertTrue(e.getMessage().contains("unknown_id"));
        }
    }

    @Test
    public void testGetListBugemonMixedIdsThrows() throws JsonResourceNotFoundException{
        try {
            provider.getListBugemon(Arrays.asList("florachu", "unknown_id", "exceflam"));
            fail("Expected BugemonNotFoundException");
        } catch (BugemonNotFoundException e) {
            assertTrue(e.getMessage().contains("unknown_id"));
        }
    }

    @Test
    public void testGetListBugemonCorrectIds() throws BugemonNotFoundException, JsonResourceNotFoundException {
        List<Bugemon> list = provider.getListBugemon(Arrays.asList("florachu", "exceflam"));
        assertEquals("florachu", list.get(0).getId());
        assertEquals("exceflam", list.get(1).getId());
    }

    @Test
    public void testGetListBugemonNoNullElements() throws BugemonNotFoundException, JsonResourceNotFoundException {
        List<Bugemon> list = provider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        for (Bugemon bug : list) {
            assertNotNull(bug);
        }
    }

    @Test
    public void testGetListBugemonAllTypesReturnsCorrectSize() throws BugemonNotFoundException, JsonResourceNotFoundException {
        List<Bugemon> list = provider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide", "rockachu"));
        assertEquals(4, list.size());
    }

    @Test
    public void testGetListBugemonHasAttacks() throws BugemonNotFoundException, JsonResourceNotFoundException {
        List<Bugemon> list = provider.getListBugemon(Arrays.asList("florachu", "exceflam", "commitide"));
        for (Bugemon bug : list) {
            assertNotNull(bug.getAttacks());
            assertFalse("Attacks should not be empty for " + bug.getId(), bug.getAttacks().isEmpty());
        }
    }

    @Test
    public void testGetListBugemonAttacksNotNull() throws BugemonNotFoundException, JsonResourceNotFoundException {
        List<Bugemon> list = provider.getListBugemon(Arrays.asList("florachu"));
        for (Attack attack : list.get(0).getAttacks()) {
            assertNotNull(attack);
        }
    }

    @Test
    public void testGetAllBugemonsNotNull() throws JsonResourceNotFoundException {
        List<Bugemon> list = provider.getAllBugemons();
        assertNotNull(list);
    }

    @Test
    public void testGetAllBugemonsHaveAttacks() throws JsonResourceNotFoundException {
        List<Bugemon> list = provider.getAllBugemons();
        for (Bugemon bug : list) {
            assertFalse("Attacks should not be empty for " + bug.getId(), bug.getAttacks().isEmpty());
        }
    }

    @Test
    public void testGetAllBugemonsNoNullElements() throws JsonResourceNotFoundException {
        List<Bugemon> list = provider.getAllBugemons();
        for (Bugemon bug : list) {
            assertNotNull(bug);
        }
    }

    @Test
    public void testGetAllBugemonsNotEmpty() throws JsonResourceNotFoundException {
        List<Bugemon> list = provider.getAllBugemons();
        assertFalse(list.isEmpty());
    }

    @Test
    public void testGetAllBugemonsDTONotNull() throws JsonResourceNotFoundException {
        List<BugemonDTO> list = provider.getAllBugemonsDTO();
        assertNotNull(list);
    }

    @Test
    public void testGetAllBugemonsDTONotEmpty() throws JsonResourceNotFoundException {
        List<BugemonDTO> list = provider.getAllBugemonsDTO();
        assertFalse(list.isEmpty());
    }

    @Test
    public void testGetAllBugemonsDTONoNullElements() throws JsonResourceNotFoundException {
        List<BugemonDTO> list = provider.getAllBugemonsDTO();
        for (BugemonDTO dto : list) {
            assertNotNull(dto);
        }
    }
}