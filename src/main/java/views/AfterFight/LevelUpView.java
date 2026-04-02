package views.AfterFight;

import configuration.AppConfig;
import controllers.AfterFight.LevelUpController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import views.View;


import java.io.IOException;

public class LevelUpView extends View<LevelUpController> {

    @FXML private Button btnOption1;
    @FXML private Button btnOption2;
    @FXML private Button btnOption3;

    public LevelUpView() throws IOException {
        super(AppConfig.FXML_LEVELUP);
    }

    @FXML
    public void initialize() {
        btnOption1.setOnAction(e -> controller.onOptionChosen(1));
        btnOption2.setOnAction(e -> controller.onOptionChosen(2));
        btnOption3.setOnAction(e -> controller.onOptionChosen(3));
    }

    @Override
    protected String getTitle() {
        return "Level Up";
    }
}
