package views;

import configuration.AppConfig;
import controllers.TitleScreenController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * View for the main title screen.
 * Displays the game title and buttons to start a new team or manage existing ones.
 */
public class TitleScreenView extends View <TitleScreenController> {

    @FXML private Label titelLabel;
    @FXML private Button btnNewGame;
    @FXML private Button btnPreBuildTeam;
    @FXML private Button btnQuit;

    /**
     * Loads the title screen FXML and wires button actions.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public TitleScreenView() throws IOException {
        super(AppConfig.FXML_TITLESCREEN);
        this.btnNewGame.setOnAction(e -> this.controller.goToSelection());
        this.btnPreBuildTeam.setOnAction(e -> this.controller.goToPreBuildTeam());
        this.btnQuit.setOnAction(e -> this.controller.quit());
    }

    @Override
    protected String getTitle() {
        return "Bugémon";
    }
}