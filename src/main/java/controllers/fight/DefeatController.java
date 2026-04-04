package controllers.fight;

import controllers.Controller;
import controllers.ControllerManager;
import views.fight.DefeatView;

import java.io.IOException;

public class DefeatController extends Controller<DefeatView> {
    public DefeatController(ControllerManager metaController) throws IOException {
        super(metaController, new DefeatView());
        this.view.setController(this); /*Je me présente à la vue comme son contrôleur*/
    }
        public void goToMenu() {
            this.controllerManager.goToTitleScreen();
        }

}
