package models.bugemon;

import com.google.gson.Gson;
import junit.framework.TestCase;
import org.junit.Test;

public class ActiveEffectTest extends TestCase {

    private static final Gson GSON = new Gson();

    private Effect buildEffect(String duration) {
        String dur = duration == null ? "null" : "\"" + duration + "\"";
        String json = "{\"type\":\"stat_modifier\",\"cible\":\"lanceur\",\"stat\":\"attaque\","
                + "\"modificateur\":1,\"duree\":" + dur + "}";
        return GSON.fromJson(json, Effect.class);
    }

    private Effect permanentEffect() {
        return buildEffect("permanent");
    }

    private Effect timedEffect(int turns) {
        return buildEffect(turns + "_tours");
    }

    @Test
    public void testConstructorPermanentEffectHasNullTurnsLeft() {
        ActiveEffect ae = new ActiveEffect(permanentEffect());
        assertNull(ae.getTurnsLeft());
    }

    @Test
    public void testConstructorTimedEffectHasDurationPlusOneTurnsLeft() {
        ActiveEffect ae = new ActiveEffect(timedEffect(3));
        assertEquals(Integer.valueOf(4), ae.getTurnsLeft());
    }

    @Test
    public void testGetEffectReturnsWrappedEffect() {
        Effect e = timedEffect(2);
        ActiveEffect ae = new ActiveEffect(e);
        assertSame(e, ae.getEffect());
    }

    @Test
    public void testDecrementTurnsReducesCounter() {
        ActiveEffect ae = new ActiveEffect(timedEffect(3));
        ae.decrementTurns();
        assertEquals(Integer.valueOf(3), ae.getTurnsLeft());
        ae.decrementTurns();
        assertEquals(Integer.valueOf(2), ae.getTurnsLeft());
    }

    @Test
    public void testDecrementTurnsOnPermanentEffectKeepsNull() {
        ActiveEffect ae = new ActiveEffect(permanentEffect());
        ae.decrementTurns();
        ae.decrementTurns();
        assertNull(ae.getTurnsLeft());
    }

    @Test
    public void testIsExpiredFalseInitiallyForTimedEffect() {
        ActiveEffect ae = new ActiveEffect(timedEffect(3));
        assertFalse(ae.isExpired());
    }

    @Test
    public void testIsExpiredFalseForPermanentEffect() {
        ActiveEffect ae = new ActiveEffect(permanentEffect());
        assertFalse(ae.isExpired());
    }

    @Test
    public void testIsExpiredTrueWhenTurnsLeftReachesZero() {
        ActiveEffect ae = new ActiveEffect(timedEffect(2));
        ae.decrementTurns();
        ae.decrementTurns();
        ae.decrementTurns();
        assertEquals(Integer.valueOf(0), ae.getTurnsLeft());
        assertTrue(ae.isExpired());
    }

    @Test
    public void testIsExpiredStaysTrueAfterAdditionalDecrement() {
        ActiveEffect ae = new ActiveEffect(timedEffect(1));
        ae.decrementTurns();
        ae.decrementTurns();
        ae.decrementTurns();
        assertTrue(ae.isExpired());
    }
}