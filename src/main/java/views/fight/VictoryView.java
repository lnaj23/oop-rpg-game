package views.fight;

import configuration.AppConfig;
import controllers.fight.VictoryController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.bugemon.dto.BugemonDTO;
import views.View;

import java.io.IOException;
import java.util.List;

public class VictoryView extends View<VictoryController> {

    @FXML private Button continueBtw;
    @FXML private VBox bugemonXpList;
    @FXML private Label gainedEXPLabel;

    public VictoryView() throws IOException {
        super(AppConfig.FXML_VICTORY);
        this.continueBtw.setOnAction(e -> this.controller.goToMenu());
    }

    @Override
    protected String getTitle() {
        return "Bugémon - Victoire";
    }

    public void showBugemonXpList(List<BugemonDTO> bugemons, int expForEachBug) {
        bugemonXpList.getChildren().clear();
        for (BugemonDTO b : bugemons) {
            int expGainedByThisBugemon = b.isHasParticipedToTrue() ? expForEachBug : 0;
            Label l = new Label(b.getName() + ": (" + b.getcurrenPv() + "/" + b.getPv() + ") + " + expGainedByThisBugemon + "XP");
            l.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            bugemonXpList.getChildren().add(l);
        }
    }

    public void updateEXPGainedLabel(int TotalEXP) {
        String txt = "XP gagnée : " + TotalEXP;
        gainedEXPLabel.setText(txt);
    }
}
