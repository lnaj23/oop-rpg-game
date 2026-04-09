package controllers.AfterFight;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.Bugemon;
import models.game.LevelUpBonus;
import services.fight.LevelUpBonusGenerator;
import views.AfterFight.LevelUpView;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the level up screen.
 * Handles the display of the Bugemon that leveled up
 * and the bonus options proposed to the player.
 */
public class LevelUpController extends Controller<LevelUpView> {
    private int indexLevelUp;
    private int bugemonIndexLevelUp;
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

    public void startLevelUpList(List<Bugemon> bugemonToLevelUp){

        this.bugemonToLevelUp = bugemonToLevelUp;
        this.indexLevelUp = 0;
        setBugemonLevelUp();
    }

    /**
     * Updates the level up screen with the Bugemon sprite
     * and the text indicating its new level.
     *
     * @param bugemon the Bugemon that has leveled up
     */
    public void setBugemonLevelUp() {
        this.allOptions = bonusGenerator.generateOptions();

        this.currentBugemon = this.bugemonToLevelUp.get(indexLevelUp);
        if (bugemonIndexLevelUp > 0) {
            bugemonIndexLevelUp -= 1;
        }
        else{
            bugemonIndexLevelUp =
        }
        view.updateBugemonSprite(currentBugemon);
        view.updateLevelUpText(currentBugemon);
        setOptions(allOptions.get(0).toString(), allOptions.get(1).toString(), allOptions.get(2).toString());
    }

    private int countCurrentBugemonOccurrences() {
        Bugemon bugemon = bugemonToLevelUp.get(indexLevelUp);
        int count = 0;

        for (int i = indexLevelUp; i < bugemonToLevelUp.size(); i++) {
            if (bugemonToLevelUp.get(i) == bugemon) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }

     /**
     * Updates the text of the three bonus option buttons.
     *
     * @param option1 the text for the first option
     * @param option2 the text for the second option
     * @param option3 the text for the third option
     */
    public void setOptions(String option1, String option2, String option3) {
        view.updateOptionTexts(option1, option2, option3);
    }

    /**
     * Handles the player's choice of one level up option.
     *
     * @param option the chosen option number
     */
    public void onOptionChosen(int option) {
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
    }
}
