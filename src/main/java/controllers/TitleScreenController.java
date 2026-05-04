package controllers;

import views.TitleScreenView;

import java.io.IOException;

/**
 * Controller for the main title screen.
 * Handles navigation to the selection screen or team management screen.
 */
public class TitleScreenController extends Controller<TitleScreenView> {

    /**
     * @param metaController the central controller
     * @throws IOException if the FXML fails to load
     */
    public TitleScreenController(ControllerManager metaController) throws IOException {
        super(metaController, new TitleScreenView());
        this.view.setController(this);
    }

    /** Opens the selection screen to create a new team. */
    public void goToSelection() {
        this.controllerManager.openNewSelection();
    }

    /** Navigates to the team management screen. */
    public void goToPreBuildTeam() {
        this.controllerManager.goToPrebuildTeam();
    }

    public void goToAddBugemon() {
        this.controllerManager.goToAddBugemon();
    }

    /** Quit the application */
    public void quit() {
        System.exit(0);
    }


}