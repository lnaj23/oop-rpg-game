package services.dataprovider;

import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import models.bugemon.Attack;
import org.junit.Test;
import services.controller.dataprovider.AttackProvider;

import java.util.List;

public class AttackProviderTest extends TestCase {

    private final AttackProvider provider = new AttackProvider();

    @Test
    public void testGetAttackByIdReturnsNotNull() throws AttackNotFoundException, JsonResourceNotFoundException {
        Attack attack = provider.getAttackById("racines_vives");
        assertNotNull(attack);
    }

    @Test
    public void testGetAttackByIdCorrectId() throws AttackNotFoundException, JsonResourceNotFoundException {
        Attack attack = provider.getAttackById("pollen_sournois");
        assertEquals("pollen_sournois", attack.getId());
    }

    @Test
    public void testGetAttackByIdUnknownIdThrows() throws JsonResourceNotFoundException {
        try {
            provider.getAttackById("unknown_id");
            fail("Expected AttackNotFoundException");
        } catch (AttackNotFoundException e) {
            assertTrue(e.getMessage().contains("unknown_id"));
        }
    }

    @Test
    public void testGetAllAttacksNotNull() throws JsonResourceNotFoundException {
        List<Attack> list = provider.getAllAttacks();
        assertNotNull(list);
    }

    @Test
    public void testGetAllAttacksNotEmpty() throws JsonResourceNotFoundException {
        List<Attack> list = provider.getAllAttacks();
        assertFalse(list.isEmpty());
    }

    @Test
    public void testGetAllAttacksNoNullElements() throws JsonResourceNotFoundException {
        List<Attack> list = provider.getAllAttacks();
        for (Attack attack : list) {
            assertNotNull(attack);
        }
    }

    @Test
    public void testGetAllAttacksAllHaveId() throws JsonResourceNotFoundException {
        List<Attack> list = provider.getAllAttacks();
        for (Attack attack : list) {
            assertNotNull(attack.getId());
            assertFalse(attack.getId().isEmpty());
        }
    }

    @Test
    public void testGetAllAttacksIsDefensiveCopy() throws JsonResourceNotFoundException {
        List<Attack> first = provider.getAllAttacks();
        first.clear();
        List<Attack> second = provider.getAllAttacks();
        assertFalse(second.isEmpty());
    }
}