package views;

import configuration.AppConfig;
import controllers.AddBugemonController;
import controllers.afterfight.LevelUpController;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.IOException;

public class AddBugemonView extends View<AddBugemonController> {

    @FXML private Button addButton;
    @FXML private Button cancelButton;
    @FXML private TextField nameTextField;
    @FXML private TextField typeTextField;
    @FXML private TextField hpTextField;
    @FXML private TextField attackTextField;
    @FXML private TextField defenseTextField;
    @FXML private TextField speedTextField;
    @FXML private TextField spriteTextField;

    @Override
    public String getTitle() {
        return "Add Bugemon";
    }

    public AddBugemonView() throws IOException {
        super(AppConfig.FXML_ADDBUGEMON);
    }
}
