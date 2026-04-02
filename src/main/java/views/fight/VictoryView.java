package views.fight;

import configuration.AppConfig;
import controllers.fight.VictoryController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import views.View;

import java.io.IOException;

public class VictoryView extends View<VictoryController> {

    @FXML private Button btnContinue;
    @FXML private Label victoryLabel;

    public VictoryView() throws IOException {
        super(AppConfig.FXML_VICTORY);
        this.btnContinue.setOnAction(e -> this.controller.goToMenu());
    }

    @Override
    protected String getTitle() {
        return "Bugémon - Victoire";
    }
}
