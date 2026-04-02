package controllers.fight;

import controllers.Controller;
import controllers.ControllerManager;
import views.View;
import views.fight.VictoryView;

import java.io.IOException;

public class VictoryController extends Controller<VictoryView> {
    /**
     * @param metaController the central controller handling navigation
     */
    public VictoryController(ControllerManager metaController) throws IOException {
        super(metaController, new VictoryView());
        this.view.setController(this);
    }

    /** Navigates back to the main menu. */
    public void goToMenu() {
        this.controllerManager.goToTitleScreen();
    }
}
