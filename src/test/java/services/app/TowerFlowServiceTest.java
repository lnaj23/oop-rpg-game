package services.app;

import junit.framework.TestCase;

import exceptions.TowerNotInitializedException;
import models.game.LevelTower;
import org.junit.Before;
import org.junit.Test;

public class TowerFlowServiceTest extends TestCase {

    private TowerFlowService service;

    @Before
    public void setUp() {
        service = new TowerFlowService();
        service.startNewTower();
    }

    @Test
    public void testStartNewTowerInitializesGameState() {
        LevelTower tower = GameStateHolder.getInstance().getCurrentLevelTower();
        assertNotNull(tower);
        assertEquals(LevelTower.FIRST_FLOOR, tower.getCurrentFloor());
        assertEquals(0, tower.getCurrentStep());
        assertFalse(tower.isFinished());
    }

    @Test
    public void testStartNewTowerOverridesPreviousTower() {
        LevelTower first = GameStateHolder.getInstance().getCurrentLevelTower();
        service.startNewTower();
        LevelTower second = GameStateHolder.getInstance().getCurrentLevelTower();
        assertNotSame("tower not reset", first, second);
    }

    @Test
    public void testAdvanceFollowsFloorPattern() throws TowerNotInitializedException {
        assertEquals(TowerNextAction.REWARD, service.advanceToNextAction());
        assertEquals(TowerNextAction.FIGHT,  service.advanceToNextAction());
        assertEquals(TowerNextAction.FIGHT,  service.advanceToNextAction());
        assertEquals(TowerNextAction.REWARD, service.advanceToNextAction());
        assertEquals(TowerNextAction.FIGHT,  service.advanceToNextAction());
        assertEquals(TowerNextAction.FIGHT,  service.advanceToNextAction());
    }

    @Test
    public void testAdvanceReturnsTitleScreenWhenTowerFinished() throws TowerNotInitializedException {
        int totalSteps = (LevelTower.LAST_FLOOR - LevelTower.FIRST_FLOOR + 1) * 6;
        TowerNextAction last = null;
        for (int i = 0; i < totalSteps; i++) {
            last = service.advanceToNextAction();
        }
        assertEquals(TowerNextAction.TITLE_SCREEN, last);
        assertTrue("tower should be finished",
                GameStateHolder.getInstance().getCurrentLevelTower().isFinished());
    }

    @Test
    public void testAdvanceUpdatesCurrentFloor() throws TowerNotInitializedException {
        for (int i = 0; i < 6; i++) {
            service.advanceToNextAction();
        }
        assertEquals(LevelTower.FIRST_FLOOR + 1,
                GameStateHolder.getInstance().getCurrentLevelTower().getCurrentFloor());
    }
}