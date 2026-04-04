package views.fight;

import configuration.AppConfig;
import controllers.fight.DefeatController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import views.View;

import java.io.IOException;

public class DefeatView extends View<DefeatController> {
    @FXML private Button retryBtn;
    @FXML private Button giveUpBtn;

    /**
     Le cours dit que le chargement FXML peut échouer si le fichier n'existe pas
     */
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

