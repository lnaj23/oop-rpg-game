package views.tower;

import configuration.AppConfig;
import controllers.tower.RewardController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import models.game.dto.ItemDTO;
import views.View;

import java.io.IOException;

public class RewardView extends View<RewardController> {

    @FXML private Button gainItemBtn;
    @FXML private Button learnAttackBtn;
    @FXML private Button gainBonusBtn;

    public RewardView() throws IOException {
        super(AppConfig.FXML_REWARD);
        gainItemBtn.setOnAction(e -> this.controller.onGainItemBtnClicked());
        learnAttackBtn.setOnAction(e -> {this.controller.onlearnAttackBtnClicked();});
        gainBonusBtn.setOnAction(e -> {this.controller.onGainBonusBtnClicked();});
    }

    public void setRewardDisplay(ItemDTO item, String attackName, String bonus) {
        gainItemBtn.setText("Obtenir cet objet : " + "\n" + item.getName());
        learnAttackBtn.setText("Apprendre cette attaque à un Bugemon : " + "\n" + attackName);
        gainBonusBtn.setText("Appliquer ce Bonus à un Bugemon : " + "\n" + bonus);
    }

    @Override
    protected String getTitle() {
        return "Bugémon - Récompense";
    }
}
