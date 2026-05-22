package controllers.afterfight;

import controllers.Controller;
import controllers.ControllerManager;
import views.afterfight.DefeatView;

import java.io.IOException;

/**
 * Controller for the defeat screen, shown when the player loses a fight.
 * Lets the player restart a new run or return to the title screen.
 */
public class DefeatController extends Controller<DefeatView> {
    public DefeatController(ControllerManager metaController) throws IOException {
        super(metaController, new DefeatView());
        this.view.setController(this);
    }

    public void goToMenu() {
        this.controllerManager.goToTitleScreen();
    }

    public void retryRun(){
        this.controllerManager.startNewTower();
    }

}
