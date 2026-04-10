package repositories;

import models.bugemon.Bugemon;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
public class BugemonRepositoryTest {

    @Test
    public void testGetInstanceNotNull() {
        assertNotNull(BugemonRepository.getInstance());
    }

    @Test
    public void testSingletonReturnsSameInstance() {
        assertSame(BugemonRepository.getInstance(), BugemonRepository.getInstance());
    }

    @Test
    public void testGetAllBugemonsNotNull() {
        assertNotNull(BugemonRepository.getInstance().getAllBugemons());
    }

    @Test
    public void testGetAllBugemonsNotEmpty() {
        assertFalse(BugemonRepository.getInstance().getAllBugemons().isEmpty());
    }

    @Test
    public void testGetAllBugemonsNoNullElement() {
        for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
            assertNotNull(bug);
        }
    }

    @Test
    public void testEachBugemonHasNonNullId() {
        for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
            assertNotNull(bug.getId());
            assertFalse(bug.getId().isEmpty());
        }
    }

    @Test
    public void testEachBugemonHasNonNullName() {
        for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
            assertNotNull(bug.getName());
        }
    }

    @Test
    public void testEachBugemonHasAttackNames() {
        for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
            assertNotNull(bug.getAttackNamesAsList());
            assertFalse(bug.getAttackNamesAsList().isEmpty());
        }
    }
}