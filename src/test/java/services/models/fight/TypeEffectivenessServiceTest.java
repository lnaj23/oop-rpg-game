package services.models.fight;

import junit.framework.TestCase;
import models.fight.Effectiveness;
import org.junit.Test;

public class TypeEffectivenessServiceTest extends TestCase {

    @Test
    public void testMultiplierSuperEffectiveFloraAgainstAqua() {
        assertEquals(1.5, TypeEffectivenessService.getTypeMultiplier("flora", "aqua"), 0.0);
    }

    @Test
    public void testMultiplierSuperEffectiveAquaAgainstPyro() {
        assertEquals(1.5, TypeEffectivenessService.getTypeMultiplier("aqua", "pyro"), 0.0);
    }

    @Test
    public void testMultiplierSuperEffectivePyroAgainstLitho() {
        assertEquals(1.5, TypeEffectivenessService.getTypeMultiplier("pyro", "litho"), 0.0);
    }

    @Test
    public void testMultiplierSuperEffectiveLithoAgainstFlora() {
        assertEquals(1.5, TypeEffectivenessService.getTypeMultiplier("litho", "flora"), 0.0);
    }

    @Test
    public void testMultiplierNotVeryEffectiveAquaAgainstFlora() {
        assertEquals(0.75, TypeEffectivenessService.getTypeMultiplier("aqua", "flora"), 0.0);
    }

    @Test
    public void testMultiplierNotVeryEffectivePyroAgainstAqua() {
        assertEquals(0.75, TypeEffectivenessService.getTypeMultiplier("pyro", "aqua"), 0.0);
    }

    @Test
    public void testMultiplierNeutralSameType() {
        assertEquals(1.0, TypeEffectivenessService.getTypeMultiplier("flora", "flora"), 0.0);
        assertEquals(1.0, TypeEffectivenessService.getTypeMultiplier("pyro", "pyro"), 0.0);
    }

    @Test
    public void testMultiplierNeutralUnknownType() {
        assertEquals(1.0, TypeEffectivenessService.getTypeMultiplier("flora", "n_existe_pas"), 0.0);
    }

    @Test
    public void testMultiplierCaseInsensitive() {
        assertEquals(1.5, TypeEffectivenessService.getTypeMultiplier("FLORA", "Aqua"), 0.0);
    }

    @Test
    public void testMultiplierNullTypesReturnsNeutral() {
        assertEquals(1.0, TypeEffectivenessService.getTypeMultiplier(null, "aqua"), 0.0);
        assertEquals(1.0, TypeEffectivenessService.getTypeMultiplier("flora", null), 0.0);
        assertEquals(1.0, TypeEffectivenessService.getTypeMultiplier(null, null), 0.0);
    }


    @Test
    public void testEffectivenessSuperEffective() {
        assertEquals(Effectiveness.SUPER_EFFECTIVE,
                TypeEffectivenessService.getEffectiveness("flora", "aqua"));
    }

    @Test
    public void testEffectivenessNotVeryEffective() {
        assertEquals(Effectiveness.NOT_VERY_EFFECTIVE,
                TypeEffectivenessService.getEffectiveness("aqua", "flora"));
    }

    @Test
    public void testEffectivenessNeutral() {
        assertEquals(Effectiveness.NEUTRAL,
                TypeEffectivenessService.getEffectiveness("flora", "flora"));
    }

    @Test
    public void testEffectivenessNeutralForNullTypes() {
        assertEquals(Effectiveness.NEUTRAL,
                TypeEffectivenessService.getEffectiveness(null, null));
    }
}