package views;

import configuration.AppConfig;
import controllers.AddBugemonController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.bugemon.Attack;
import services.dataprovider.AttackProvider;

import java.io.IOException;
import java.util.List;

public class AddBugemonView extends View<AddBugemonController> {

    @FXML private Button btnAdd;
    @FXML private Button btnCancel;
    @FXML private Button btnChooseSprite;

    @FXML private Label titleLabel;

    @FXML private TextField nameField;
    @FXML private TextField hpField;
    @FXML private TextField attackField;
    @FXML private TextField defenseField;
    @FXML private TextField spritePathField;

    @FXML private ChoiceBox<?> typeChoiceBox;
    @FXML private ChoiceBox<Attack> attack1ChoiceBox;
    @FXML private ChoiceBox<Attack> attack2ChoiceBox;
    @FXML private ChoiceBox<Attack> attack3ChoiceBox;
    @FXML private ChoiceBox<?> starterChoiceBox;

    public AddBugemonView() throws IOException {
        super(AppConfig.FXML_ADDBUGEMON);
    }

    public void setActionButtons() {
        btnCancel.setOnAction(e -> this.controller.goToTitleScreen());
        btnAdd.setOnAction(e -> this.controller.addBugemon());
    }

    public void setAttackChoiceBox(List<Attack> attacks) {
        attack1ChoiceBox.getItems().setAll(attacks);
        attack2ChoiceBox.getItems().setAll(attacks);
        attack3ChoiceBox.getItems().setAll(attacks);
    }

    @Override
    public String getTitle() {
        return "Add Bugemon";
    }
}
