package controllers.fight;

import controllers.Controller;
import controllers.ControllerManager;
import views.View;
import views.fight.VictoryView;

public class VictoryController extends Controller<VictoryView> {
    /**
     * @param metaController the central controller handling navigation
     * @param view           the view managed by this controller
     */
    public VictoryController(ControllerManager metaController, View view) {
        super(metaController, (VictoryView) view);
    }
    /** Navigates back to the main menu. */
    public void goToMenu() {
        this.controllerManager.goToTitleScreen();
    }
}
