package views.afterfight;

import configuration.AppConfig;
import controllers.afterfight.DefeatController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import views.View;

import java.io.IOException;

/**
 * View for the defeat screen. Displays the retry and give-up buttons and
 * delegates clicks to its {@link DefeatController}.
 */
public class DefeatView extends View {

    private DefeatController controller;

    /**
     * Sets the controller used by this view to delegate user actions.
     *
     * @param controller the controller associated with this view
     */
    public void setController(DefeatController controller) {
        this.controller = controller;
    }
    @FXML private Button retryBtn;
    @FXML private Button giveUpBtn;

public DefeatView() throws IOException{
        super(AppConfig.FXML_DEFEAT);
        this.retryBtn.setOnAction(M -> this.controller.retryRun());
        this.giveUpBtn.setOnAction(M -> this.controller.goToMenu());
    }

    @Override
    protected String getTitle() {
        return "Bugémon - Défaite...";
    }
}

