package repositories;

import exceptions.JsonResourceNotFoundException;
import models.bugemon.Attack;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class AttackRepositoryTest {

    @Test
    public void testGetInstanceNotNull() throws JsonResourceNotFoundException {
        assertNotNull(AttackRepository.getInstance());
    }

    @Test
    public void testSingletonReturnsSameInstance() throws JsonResourceNotFoundException {
        assertSame(AttackRepository.getInstance(), AttackRepository.getInstance());
    }

    @Test
    public void testGetAllAttacksNotNull() throws JsonResourceNotFoundException {
        assertNotNull(AttackRepository.getInstance().getAllAttacks());
    }

    @Test
    public void testGetAllAttacksNotEmpty() throws JsonResourceNotFoundException {
        assertFalse(AttackRepository.getInstance().getAllAttacks().isEmpty());
    }

    @Test
    public void testGetAllAttacksNoNullElement() throws JsonResourceNotFoundException {
        for (Attack attack : AttackRepository.getInstance().getAllAttacks()) {
            assertNotNull(attack);
        }
    }

    @Test
    public void testEachAttackHasNonNullId() throws JsonResourceNotFoundException {
        for (Attack attack : AttackRepository.getInstance().getAllAttacks()) {
            assertNotNull(attack.getId());
            assertFalse(attack.getId().isEmpty());
        }
    }

    @Test
    public void testEachAttackHasNonNullName() throws JsonResourceNotFoundException {
        for (Attack attack : AttackRepository.getInstance().getAllAttacks()) {
            assertNotNull(attack.getName());
        }
    }

    @Test
    public void testEachAttackHasPositiveDamage() throws JsonResourceNotFoundException {
        for (Attack attack : AttackRepository.getInstance().getAllAttacks()) {
            assertTrue(attack.getPower() >= 0);
        }
    }
}