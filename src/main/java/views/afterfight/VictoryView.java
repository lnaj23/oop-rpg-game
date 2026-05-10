package views.afterfight;

import configuration.AppConfig;
import controllers.afterfight.VictoryController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.bugemon.dto.BugemonDTO;
import views.View;

import java.io.IOException;
import java.util.List;

public class VictoryView extends View {

    private VictoryController controller;

    /**
     * Sets the controller used by this view to delegate user actions.
     *
     * @param controller the controller associated with this view
     */
    public void setController(VictoryController controller) {
        this.controller = controller;
    }

    @FXML private Button continueBtn;
    @FXML private VBox bugemonXpList;
    @FXML private Label gainedEXPLabel;

    public VictoryView() throws IOException {
        super(AppConfig.FXML_VICTORY);
        this.continueBtn.setOnAction(e -> this.controller.continueTower());
    }

    @Override
    protected String getTitle() {
        return "Bugémon - Victoire";
    }

    public void showBugemonXpList(List<BugemonDTO> bugemons, int expForEachBug) {
        bugemonXpList.getChildren().clear();
        for (BugemonDTO b : bugemons) {
            int expGainedByThisBugemon = b.hasParticipated() ? expForEachBug : 0;
            Label l = new Label(b.getName() + ": (" + b.getCurrenPv() + "/" + b.getPv() + ") + " + expGainedByThisBugemon + "XP");
            l.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            bugemonXpList.getChildren().add(l);
        }
    }

    public void updateEXPGainedLabel(int TotalEXP) {
        String txt = "XP gagnée : + " + TotalEXP;
        gainedEXPLabel.setText(txt);
    }
}
