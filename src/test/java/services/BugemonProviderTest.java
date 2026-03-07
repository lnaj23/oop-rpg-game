package services;

import exceptions.BugemonNotFoundException;
import junit.framework.TestCase;
import models.Bugemon.Bugemon;
import org.junit.Test;

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
    public void testGetSTATSPvReturnsCorrectValue() {
        assertEquals("90", BugemonProvider.getSTATS("florachu", "pv"));
    }

    @Test
    public void testGetSTATSAttaqueReturnsCorrectValue() {
        assertEquals("55", BugemonProvider.getSTATS("florachu", "attaque"));
    }

    @Test
    public void testGetSTATSDefenseReturnsCorrectValue() {
        assertEquals("40", BugemonProvider.getSTATS("florachu", "defense"));
    }

    @Test
    public void testGetSTATSInitiativeReturnsCorrectValue() {
        assertEquals("50", BugemonProvider.getSTATS("florachu", "initiative"));
    }

    @Test
    public void testGetSTATSAttaquesReturnsCorrectValue() {
        assertEquals("fouet_liane, pollen_sournois, racines_vives", BugemonProvider.getSTATS("florachu", "attaques"));
    }

    @Test
    public void testGetSTATSUnknownStatReturnsNull() {
        assertNull(BugemonProvider.getSTATS("florachu", "unknown_stat"));
    }

    @Test
    public void testGetSTATSUnknownBugemonThrows() {
        try {
            BugemonProvider.getSTATS("unknown_id", "pv");
            fail("Expected BugemonNotFoundException");
        } catch (BugemonNotFoundException e) {
            assertTrue(e.getMessage().contains("unknown_id"));
        }
    }

    @Test
    public void testGetSTATSUnknownBugemonThrowsForEachStat() {
        for (String stat : Arrays.asList("pv", "attaque", "defense", "initiative", "attaques")) {
            try {
                BugemonProvider.getSTATS("unknown_id", stat);
                fail("Expected BugemonNotFoundException for stat: " + stat);
            } catch (BugemonNotFoundException e) {
                assertTrue(e.getMessage().contains("unknown_id"));
            }
        }
    }


    @Test
    public void testGetSTATSPvDifferentBugemon() {
        assertEquals("85", BugemonProvider.getSTATS("exceflam", "pv"));
    }

    @Test
    public void testGetSTATSAttaquesIsNotEmpty() {
        String result = BugemonProvider.getSTATS("florachu", "attaques");
        assertFalse(result.isEmpty());
    }
}