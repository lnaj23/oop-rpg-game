package repositories;

import exceptions.JsonResourceNotFoundException;
import models.bugemon.Bugemon;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
public class BugemonRepositoryTest {

    @Test
    public void testGetInstanceNotNull() throws JsonResourceNotFoundException {
        assertNotNull(BugemonRepository.getInstance());
    }

    @Test
    public void testSingletonReturnsSameInstance() throws JsonResourceNotFoundException {
        assertSame(BugemonRepository.getInstance(), BugemonRepository.getInstance());
    }

    @Test
    public void testGetAllBugemonsNotNull() throws JsonResourceNotFoundException {
        assertNotNull(BugemonRepository.getInstance().getAllBugemons());
    }

    @Test
    public void testGetAllBugemonsNotEmpty() throws JsonResourceNotFoundException {
        assertFalse(BugemonRepository.getInstance().getAllBugemons().isEmpty());
    }

    @Test
    public void testGetAllBugemonsNoNullElement() throws JsonResourceNotFoundException {
        for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
            assertNotNull(bug);
        }
    }

    @Test
    public void testEachBugemonHasNonNullId() throws JsonResourceNotFoundException {
        for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
            assertNotNull(bug.getId());
            assertFalse(bug.getId().isEmpty());
        }
    }

    @Test
    public void testEachBugemonHasNonNullName() throws JsonResourceNotFoundException {
        for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
            assertNotNull(bug.getName());
        }
    }

    @Test
    public void testEachBugemonHasAttackNames() throws JsonResourceNotFoundException {
        for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
            assertNotNull(bug.getAttackNamesAsList());
            assertFalse(bug.getAttackNamesAsList().isEmpty());
        }
    }
}