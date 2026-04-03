package controllers.AfterFight;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.dto.BugemonDTO;
import views.AfterFight.LevelUpView;

import java.io.IOException;

/**
 * Controller for the level up screen.
 * Handles the display of the Bugemon that leveled up
 * and the bonus options proposed to the player.
 */
public class LevelUpController extends Controller<LevelUpView> {

    /**
     * Creates the controller and initializes the associated level up view.
     *
     * @param metaController the main controller manager of the application
     * @throws IOException if the level up view cannot be loaded
     */
    public LevelUpController(ControllerManager metaController) throws IOException {
        super(metaController, new LevelUpView());
        this.view.setController(this);
    }

    /**
     * Handles the player's choice of one level up option.
     *
     * @param option the chosen option number
     */
    public void onOptionChosen(int option) {
      // model.applyLevelUpChoice(option);
    }

    /**
     * Updates the level up screen with the Bugemon sprite
     * and the text indicating its new level.
     *
     * @param bugemon the Bugemon that has leveled up
     * @param level the new level reached by the Bugemon
     */
    public void setBugemonLevelUp(BugemonDTO bugemon, int level) {
        view.updateBugemonSprite(bugemon);
        view.updateLevelUpText(bugemon, level);
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
}
