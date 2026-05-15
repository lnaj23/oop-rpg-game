package services.models.fight;

import exceptions.ItemNotFoundException;
import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import models.bugemon.Bugemon;
import models.game.Item;
import org.junit.Before;
import org.junit.Test;
import services.controller.dataprovider.ItemProvider;
import services.models.fight.ItemEffectApplier;

import java.util.ArrayList;

public class ItemEffectApplierTest extends TestCase {

    private Bugemon bugemon;
    private Item baieRevigorante;
    private Item baieTonique;
    private Item serumOffensif;
    private Item gelDefensif;
    private Item pierreDeRetraite;

    @Before
    public void setUp() throws ItemNotFoundException, JsonResourceNotFoundException {
        bugemon = new Bugemon("dummy", "Flora", 100, 50, 50, 50,
                false, "", new ArrayList<>(), new ArrayList<>());
        bugemon.initPv();

        ItemProvider provider = new ItemProvider();
        baieRevigorante  = provider.getItemById("baie_revigorante");
        baieTonique      = provider.getItemById("baie_tonique");
        serumOffensif    = provider.getItemById("serum_offensif");
        gelDefensif      = provider.getItemById("gel_defensif");
        pierreDeRetraite = provider.getItemById("pierre_de_retraite");
    }

    @Test
    public void testHealItemRestoresPv() {
        bugemon.takeDamage(40);
        assertEquals(60, bugemon.getCurrentPv());
        ItemEffectApplier.ApplyItems(bugemon, baieRevigorante);
        assertEquals(80, bugemon.getCurrentPv());
    }

    @Test
    public void testHealItemCapsAtMaxPv() {
        bugemon.takeDamage(10);
        assertEquals(90, bugemon.getCurrentPv());
        ItemEffectApplier.ApplyItems(bugemon, baieTonique);
        assertEquals(100, bugemon.getCurrentPv());
    }

    @Test
    public void testBoostItemIncreasesAttackModifier() {
        int before = bugemon.getAttaque();
        ItemEffectApplier.ApplyItems(bugemon, serumOffensif);
        assertEquals(before + serumOffensif.getModifier(), bugemon.getAttaque());
    }

    @Test
    public void testBoostItemIncreasesDefenseModifier() {
        int before = bugemon.getDefense();
        ItemEffectApplier.ApplyItems(bugemon, gelDefensif);
        assertEquals(before + gelDefensif.getModifier(), bugemon.getDefense());
    }

    @Test
    public void testUnknownCategoryDoesNothing() {
        int hp = bugemon.getCurrentPv();
        int atk = bugemon.getAttaque();
        ItemEffectApplier.ApplyItems(bugemon, pierreDeRetraite);
        assertEquals(hp, bugemon.getCurrentPv());
        assertEquals(atk, bugemon.getAttaque());
    }
}