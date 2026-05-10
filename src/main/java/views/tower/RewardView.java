package views.tower;

import configuration.AppConfig;
import controllers.tower.RewardController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import models.bugemon.Attack;
import models.game.LevelUpBonus;
import models.game.dto.ItemDTO;
import services.view.formatter.LevelUpBonusFormatter;
import views.View;

import java.io.IOException;

public class RewardView extends View {

    private RewardController controller;

    /**
     * Sets the controller used by this view to delegate user actions.
     *
     * @param controller the controller associated with this view
     */
    public void setController(RewardController controller) {
        this.controller = controller;
    }

    @FXML private Button gainItemBtn;
    @FXML private Button learnAttackBtn;
    @FXML private Button gainBonusBtn;

    public RewardView() throws IOException {
        super(AppConfig.FXML_REWARD);
        gainItemBtn.setOnAction(e -> this.controller.onGainItemBtnClicked());
        learnAttackBtn.setOnAction(e -> this.controller.onlearnAttackBtnClicked());
        gainBonusBtn.setOnAction(e -> this.controller.onGainBonusBtnClicked());
    }

    /**
     * Updates the 3 reward buttons with the current rewards.
     * The view formats the raw model objects internally using its own helpers.
     */
    public void setRewardDisplay(ItemDTO item, Attack attack, LevelUpBonus bonus) {
        gainItemBtn.setText("Obtenir cet objet :\n" + item.getName());
        learnAttackBtn.setText("Apprendre cette attaque à un Bugemon :\n" + attack.getName());
        gainBonusBtn.setText("Appliquer ce Bonus à un Bugemon :\n" + LevelUpBonusFormatter.format(bonus));
    }

    @Override
    protected String getTitle() {
        return "Bugémon - Récompense";
    }
}