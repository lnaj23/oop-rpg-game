package controllers.afterfight;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.Bugemon;
import models.game.LevelUpBonus;
import services.models.game.LevelUpBonusGenerator;
import views.afterfight.LevelUpView;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the level up screen.
 * Handles the display of the Bugemon that leveled up
 * and the bonus options proposed to the player.
 */
public class LevelUpController extends Controller<LevelUpView> {

    private int indexLevelUp;
    private List<Bugemon> bugemonToLevelUp;
    private Bugemon currentBugemon;
    private List<LevelUpBonus> allOptions;
    private final LevelUpBonusGenerator bonusGenerator;

    /**
     * Creates the controller and initializes the associated level up view.
     *
     * @param metaController the main controller manager of the application
     * @throws IOException if the level up view cannot be loaded
     */
    public LevelUpController(ControllerManager metaController) throws IOException {
        super(metaController, new LevelUpView());
        this.view.setController(this);
        this.bonusGenerator = new LevelUpBonusGenerator();
    }

    public void startLevelUpList(List<Bugemon> bugemonToLevelUp) {
        this.bugemonToLevelUp = bugemonToLevelUp;
        this.indexLevelUp = 0;

        if (bugemonToLevelUp == null || bugemonToLevelUp.isEmpty()) {
            controllerManager.goToNextTowerStep();
            return;
        }
        try {
            setBugemonLevelUp();
        } catch (IOException e) {
            this.controllerManager.fatalError("Error lors du chargement du level up");
        }
    }

    /**
     * Updates the level up screen with the Bugemon sprite
     * and the text indicating its new level.
     */
    public void setBugemonLevelUp() throws IOException {
        this.currentBugemon = this.bugemonToLevelUp.get(indexLevelUp);
        this.allOptions = bonusGenerator.generateOptions();

        int totalOccurrencesForThisBugemon = countTotalOccurrencesInCurrentBlock();
        int currentOccurrenceIndex = countCurrentOccurrenceIndexInBlock();
        int displayedLevel = currentBugemon.getLevel() - totalOccurrencesForThisBugemon + currentOccurrenceIndex;
        view.updateBugemonSprite(currentBugemon);
        view.updateLevelUpText(currentBugemon, displayedLevel);
        view.updateOptions(allOptions);
    }

    /**
     * Counts how many consecutive times the current Bugemon
     * appears in the level up list in its full block.
     *
     * @return the number of occurrences for the current Bugemon block
     */
    private int countTotalOccurrencesInCurrentBlock() {
        Bugemon bugemon = bugemonToLevelUp.get(indexLevelUp);

        int start = indexLevelUp;
        while (start > 0 && bugemonToLevelUp.get(start - 1) == bugemon) {
            start--;
        }

        int end = indexLevelUp;
        while (end + 1 < bugemonToLevelUp.size() && bugemonToLevelUp.get(end + 1) == bugemon) {
            end++;
        }

        return end - start + 1;
    }

    /**
     * Counts the position of the current level up
     * inside the consecutive block of the same Bugemon.
     * <p>
     * Example:
     * if the same Bugemon appears 3 times in a row,
     * this method returns 1, then 2, then 3.
     *
     * @return the current occurrence index in the Bugemon block
     */
    private int countCurrentOccurrenceIndexInBlock() {
        Bugemon bugemon = bugemonToLevelUp.get(indexLevelUp);
        int start = indexLevelUp;

        while (start > 0 && bugemonToLevelUp.get(start - 1) == bugemon) {
            start--;
        }

        return indexLevelUp - start + 1;
    }

    /**
     * Handles the player's choice of one level up option.
     *
     * @param option the chosen option number
     */
    public void onOptionChosen(int option) {
        try {
            // Apply level up stats
            LevelUpBonus chosenBonus = allOptions.get(option - 1);
            chosenBonus.applyTo(currentBugemon);

            indexLevelUp++;
            if (indexLevelUp < bugemonToLevelUp.size()) {
                setBugemonLevelUp();
            } else {
                indexLevelUp = 0;
                controllerManager.goToNextTowerStep();
            }
        } catch (IOException e) {
            this.controllerManager.fatalError("Error lors du chargement du level up");
        }
    }
}