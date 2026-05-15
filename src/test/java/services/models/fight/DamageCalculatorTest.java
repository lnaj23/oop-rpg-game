package services.models.fight;
import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import models.bugemon.Attack;
import models.bugemon.Bugemon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.controller.dataprovider.AttackProvider;

import java.util.ArrayList;
import java.util.Random;

public class DamageCalculatorTest extends TestCase {

    private static final Random ALWAYS_NO_CRIT = new Random() {
        @Override public int nextInt(int bound) { return 99; }
    };
    private static final Random ALWAYS_CRIT = new Random() {
        @Override public int nextInt(int bound) { return 0; }
    };

    private Attack feuilleTranchante;
    private Attack explosionArdente;
    private Attack jetDePierres;
    private Attack eauPure;

    @Before
    public void setUp() throws AttackNotFoundException, JsonResourceNotFoundException {
        AttackProvider attackProvider = new AttackProvider();
        feuilleTranchante = attackProvider.getAttackById("feuille_tranchante");
        explosionArdente  = attackProvider.getAttackById("explosion_ardente");
        jetDePierres      = attackProvider.getAttackById("jet_de_pierres");
        eauPure           = attackProvider.getAttackById("eau_pure");
        DamageCalculator.setRandom(ALWAYS_NO_CRIT);
    }

    @After
    public void tearDown() {
        DamageCalculator.setRandom(new Random());
    }

    private Bugemon buildBugemon(String type, int pv, int attack, int defense, int initiative) {
        return new Bugemon("dummy_" + type, type, pv, attack, defense, initiative,
                false, "", new ArrayList<>(), new ArrayList<>());
    }

    private int expectedDamage(Bugemon attacker, Bugemon target, Attack attack,
                               double typeMultiplier, boolean critical) {
        double attackFactor = (100.0 + attacker.getAttaque()) / 100.0;
        double reductionFactor = 100.0 / (100.0 + target.getDefense());
        double damage = attack.getPower() * attackFactor * reductionFactor * typeMultiplier;
        if (critical) damage *= 1.5;
        return (int) damage;
    }

    @Test
    public void testNoCriticalGivesBaseDamage() {
        Bugemon attacker = buildBugemon("Flora", 100, 50, 0, 50);
        Bugemon target   = buildBugemon("Flora", 100, 0,  0, 50);

        int expected = expectedDamage(attacker, target, feuilleTranchante, 1.0, false);
        assertEquals(expected,
                DamageCalculator.computeDamage(attacker, target, feuilleTranchante));
    }

    @Test
    public void testCriticalDealsOneAndHalfTimesBaseDamage() {
        DamageCalculator.setRandom(ALWAYS_CRIT);
        Bugemon attacker = buildBugemon("Flora", 100, 50, 0, 50);
        Bugemon target   = buildBugemon("Flora", 100, 0,  0, 50);

        int base = expectedDamage(attacker, target, feuilleTranchante, 1.0, false);
        int crit = expectedDamage(attacker, target, feuilleTranchante, 1.0, true);
        assertEquals(crit,
                DamageCalculator.computeDamage(attacker, target, feuilleTranchante));
        assertTrue("crit not greater than base", crit > base);
    }

    @Test
    public void testSuperEffectiveTypeIncreasesDamage() {
        Bugemon attacker   = buildBugemon("Flora", 100, 0, 0, 50);
        Bugemon neutralTgt = buildBugemon("Flora", 100, 0, 0, 50);
        Bugemon weakTgt    = buildBugemon("Aqua",  100, 0, 0, 50);

        int neutral = DamageCalculator.computeDamage(attacker, neutralTgt, feuilleTranchante);
        int weak    = DamageCalculator.computeDamage(attacker, weakTgt,    feuilleTranchante);
        assertTrue("super effective <= neutral", weak > neutral);
    }

    @Test
    public void testNotVeryEffectiveTypeDecreasesDamage() {
        Bugemon attacker  = buildBugemon("Pyro", 100, 0, 0, 50);
        Bugemon strongTgt = buildBugemon("Aqua", 100, 0, 0, 50);

        int expected = expectedDamage(attacker, strongTgt, explosionArdente, 0.75, false);
        assertEquals(expected,
                DamageCalculator.computeDamage(attacker, strongTgt, explosionArdente));
    }

    @Test
    public void testHigherAttackerStatYieldsMoreDamage() {
        Bugemon weak   = buildBugemon("Litho", 100, 0,   0, 50);
        Bugemon strong = buildBugemon("Litho", 100, 100, 0, 50);
        Bugemon target = buildBugemon("Litho", 100, 0,   0, 50);

        assertTrue("higher attack should deal more damage",
                DamageCalculator.computeDamage(strong, target, jetDePierres)
                        > DamageCalculator.computeDamage(weak, target, jetDePierres));
    }

    @Test
    public void testHigherDefenderStatYieldsLessDamage() {
        Bugemon attacker = buildBugemon("Litho", 100, 50, 0,   50);
        Bugemon soft     = buildBugemon("Litho", 100, 0,  0,   50);
        Bugemon armored  = buildBugemon("Litho", 100, 0,  100, 50);

        assertTrue("higher defense should reduce damage",
                DamageCalculator.computeDamage(attacker, armored, jetDePierres)
                        < DamageCalculator.computeDamage(attacker, soft, jetDePierres));
    }

    @Test
    public void testLowPowerAttackDealsLowDamage() {
        Bugemon attacker = buildBugemon("Aqua", 100, 0, 0,   50);
        Bugemon armored  = buildBugemon("Aqua", 100, 0, 200, 50);

        int dmg = DamageCalculator.computeDamage(attacker, armored, eauPure);
        assertTrue("low power damage too high", dmg <= 5);
    }
}