package controllers.afterfight;

import controllers.Controller;
import controllers.ControllerManager;
import javafx.stage.Stage;
import models.GameState;
import views.afterfight.DefeatView;

import java.io.IOException;

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
