package views.tower;

import configuration.AppConfig;
import controllers.tower.RewardController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import views.View;

import java.io.IOException;

public class RewardView extends View<RewardController> {

    @FXML private Label rewardLabel;
    @FXML private Button continueButton;

    public RewardView() throws IOException {
        super(AppConfig.FXML_REWARD);
        continueButton.setOnAction(e -> this.controller.onContinueClicked());
    }

    public void setRewardText(String text) {
        rewardLabel.setText(text);
    }

    @Override
    protected String getTitle() {
        return "Bugémon — Récompense";
    }
}
