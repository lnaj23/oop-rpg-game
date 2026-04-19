package controllers;

import views.AddBugemonView;

import java.io.IOException;

public class AddBugemonController extends Controller<AddBugemonView> {

    public AddBugemonController(ControllerManager metaController) throws IOException {
        super(metaController, new AddBugemonView());
        this.view.setController(this);
        this.view.setActionButtons();
    }

    public void addBugemon(){

    }

    /** Navigates back to the main menu. */
    public void goToTitleScreen() {
        this.controllerManager.goToTitleScreen();
    }
}
