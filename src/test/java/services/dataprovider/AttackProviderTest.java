package services.dataprovider;

import exceptions.AttackNotFoundException;
import junit.framework.TestCase;
import models.bugemon.Attack;
import org.junit.Test;

import java.util.List;

public class AttackProviderTest extends TestCase {

    @Test
    public void testGetAttackByIdReturnsNotNull() throws AttackNotFoundException {
        Attack attack = AttackProvider.getAttackById("racines_vives");
        assertNotNull(attack);
    }

    @Test
    public void testGetAttackByIdCorrectId() throws AttackNotFoundException {
        Attack attack = AttackProvider.getAttackById("pollen_sournois");
        assertEquals("pollen_sournois", attack.getId());
    }

    @Test
    public void testGetAttackByIdUnknownIdThrows() {
        try {
            AttackProvider.getAttackById("unknown_id");
            fail("Expected AttackNotFoundException");
        } catch (AttackNotFoundException e) {
            assertTrue(e.getMessage().contains("unknown_id"));
        }
    }

    @Test
    public void testGetAllAttacksNotNull() {
        List<Attack> list = AttackProvider.getAllAttacks();
        assertNotNull(list);
    }

    @Test
    public void testGetAllAttacksNotEmpty() {
        List<Attack> list = AttackProvider.getAllAttacks();
        assertFalse(list.isEmpty());
    }

    @Test
    public void testGetAllAttacksNoNullElements() {
        List<Attack> list = AttackProvider.getAllAttacks();
        for (Attack attack : list) {
            assertNotNull(attack);
        }
    }

    @Test
    public void testGetAllAttacksAllHaveId() {
        List<Attack> list = AttackProvider.getAllAttacks();
        for (Attack attack : list) {
            assertNotNull(attack.getId());
            assertFalse(attack.getId().isEmpty());
        }
    }

    @Test
    public void testGetAllAttacksIsDefensiveCopy() {
        List<Attack> first = AttackProvider.getAllAttacks();
        first.clear();
        List<Attack> second = AttackProvider.getAllAttacks();
        assertFalse(second.isEmpty());
    }

}