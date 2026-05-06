package controllers.afterfight;

import controllers.Controller;
import controllers.ControllerManager;
import javafx.stage.Stage;
import models.GameState;
import services.Music_player;
import views.afterfight.DefeatView;

import java.io.IOException;
import java.io.Serial;

public class DefeatController extends Controller<DefeatView> {
    public DefeatController(ControllerManager metaController) throws IOException {
        super(metaController, new DefeatView());
        this.view.setController(this);
    }

    @Override
    public void show(Stage stage) {
        Music_player.chose_ambiance(GameState.DEFEAT);
        super.show(stage);
    }

    public void goToMenu() {
        this.controllerManager.goToTitleScreen();
    }

    public void retryRun(){
        this.controllerManager.startNewTower();
    }

}
