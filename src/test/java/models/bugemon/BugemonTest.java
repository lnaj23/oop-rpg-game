package models.bugemon;

import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;
import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import services.models.dataprovider.AttackProvider;
import services.models.dataprovider.BugemonProvider;

import java.util.List;

public class BugemonTest extends TestCase {

    private BugemonProvider bugemonProvider;
    private AttackProvider attackProvider;
    private Bugemon florachu;

    @Before
    public void setUp() throws AttackNotFoundException, JsonResourceNotFoundException, BugemonNotFoundException {
        bugemonProvider = new BugemonProvider();
        attackProvider = new AttackProvider();
        florachu = bugemonProvider.getListBugemon(List.of("florachu")).get(0);
    }

    @Test
    public void testHasAttackReturnsTrueForKnownAttack() {
        String knownAttackId = florachu.getAttacks().get(0).getId();
        assertTrue(florachu.hasAttack(knownAttackId));
    }

    @Test
    public void testHasAttackReturnsFalseForUnknownAttackId() {
        assertFalse(florachu.hasAttack("attaque_inexistante"));
    }

    @Test
    public void testReplaceAttackWithExistingAttack() throws AttackNotFoundException, JsonResourceNotFoundException {
        Attack oldAttack = florachu.getAttacks().get(0);
        Attack newAttack = attackProvider.getAttackById("spore_collante");

        florachu.replaceAttack(newAttack, oldAttack);

        assertEquals(newAttack.getId(), florachu.getAttacks().get(0).getId());
        assertFalse(florachu.hasAttack(oldAttack.getId()));
        assertTrue(florachu.hasAttack(newAttack.getId()));
    }

    @Test
    public void testReplaceAttackUpdatesAttackNames() throws AttackNotFoundException, JsonResourceNotFoundException {
        Attack oldAttack = florachu.getAttacks().get(0);
        Attack newAttack = attackProvider.getAttackById("spore_collante");

        florachu.replaceAttack(newAttack, oldAttack);

        assertEquals("spore_collante", florachu.getAttackNamesAsList().get(0));
    }

}
