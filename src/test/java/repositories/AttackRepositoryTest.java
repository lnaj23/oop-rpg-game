package repositories;

import models.bugemon.Attack;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class AttackRepositoryTest {

    @Test
    public void testGetInstanceNotNull() {
        assertNotNull(AttackRepository.getInstance());
    }

    @Test
    public void testSingletonReturnsSameInstance() {
        assertSame(AttackRepository.getInstance(), AttackRepository.getInstance());
    }

    @Test
    public void testGetAllAttacksNotNull() {
        assertNotNull(AttackRepository.getInstance().getAllAttacks());
    }

    @Test
    public void testGetAllAttacksNotEmpty() {
        assertFalse(AttackRepository.getInstance().getAllAttacks().isEmpty());
    }

    @Test
    public void testGetAllAttacksNoNullElement() {
        for (Attack attack : AttackRepository.getInstance().getAllAttacks()) {
            assertNotNull(attack);
        }
    }

    @Test
    public void testEachAttackHasNonNullId() {
        for (Attack attack : AttackRepository.getInstance().getAllAttacks()) {
            assertNotNull(attack.getId());
            assertFalse(attack.getId().isEmpty());
        }
    }

    @Test
    public void testEachAttackHasNonNullName() {
        for (Attack attack : AttackRepository.getInstance().getAllAttacks()) {
            assertNotNull(attack.getName());
        }
    }

    @Test
    public void testEachAttackHasPositiveDamage() {
        for (Attack attack : AttackRepository.getInstance().getAllAttacks()) {
            assertTrue(attack.getPower() >= 0);
        }
    }
}