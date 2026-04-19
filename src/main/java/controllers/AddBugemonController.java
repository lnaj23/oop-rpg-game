package controllers;

import services.game.LevelUpBonusGenerator;
import views.AddBugemonView;

import java.io.IOException;

public class AddBugemonController extends Controller<AddBugemonView> {

    public AddBugemonController(ControllerManager metaController) throws IOException {
        super(metaController, new AddBugemonView());
        this.view.setController(this);
    }
}
