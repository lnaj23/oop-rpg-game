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

    @Test
    public void testReturnedListIsDefensiveCopy() {
        List<Bugemon> list1 = BugemonRepository.getInstance().getAllBugemons();
        List<Bugemon> list2 = BugemonRepository.getInstance().getAllBugemons();
        assertNotSame(list1, list2);
    }
}