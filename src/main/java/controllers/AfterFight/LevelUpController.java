package controllers.AfterFight;

import controllers.Controller;
import controllers.ControllerManager;
import views.AfterFight.LevelUpView;

import java.io.IOException;

public class LevelUpController extends Controller<LevelUpView> {

    public LevelUpController(ControllerManager metaController) throws IOException {
        super(metaController, new LevelUpView());
        this.view.setController(this);
    }
}
