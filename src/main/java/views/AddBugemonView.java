package views;

import configuration.AppConfig;
import controllers.AddBugemonController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    @FXML private ChoiceBox<String> typeChoiceBox;
    @FXML private ChoiceBox<Attack> attack1ChoiceBox;
    @FXML private ChoiceBox<Attack> attack2ChoiceBox;
    @FXML private ChoiceBox<Attack> attack3ChoiceBox;
    @FXML private ChoiceBox<String> starterChoiceBox;

    public AddBugemonView() throws IOException {
        super(AppConfig.FXML_ADDBUGEMON);
    }

    public void setActionButtons() {
        btnCancel.setOnAction(e -> this.controller.goToTitleScreen());
        btnAdd.setOnAction(e -> this.controller.addBugemon());
        btnChooseSprite.setOnAction(e -> this.controller.chooseSprite());
    }

    public void setAttackChoiceBox(List<Attack> attacks) {
        attack1ChoiceBox.getItems().setAll(attacks);
        attack2ChoiceBox.getItems().setAll(attacks);
        attack3ChoiceBox.getItems().setAll(attacks);
    }

    public void setTypeChoiceBox() {
        List<String> types = List.of("Flora", "Pyro", "Aqua", "Litho");
        typeChoiceBox.getItems().setAll(types);
    }

    public void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ajout d'un Bugémon");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setStarterChoiceBox() {
        List<String> yesno = List.of("Oui", "Non");
        starterChoiceBox.getItems().setAll(yesno);
    }

    public void setSpritePath(String path) {
        spritePathField.setText(path);
    }

    public Button getBtnChooseSprite() {
        return btnChooseSprite;
    }

    @Override
    public String getTitle() {
        return "Add Bugemon";
    }

    public String getNameValue() {
        return nameField.getText();
    }

    public String getHpValue() {
        return hpField.getText();
    }

    public String getAttackValue() {
        return attackField.getText();
    }

    public String getDefenseValue() {
        return defenseField.getText();
    }

    public String getSpritePathValue() {
        return spritePathField.getText();
    }

    public String getSelectedType() {
        return typeChoiceBox.getValue();
    }

    public Attack getSelectedAttack1() {
        return attack1ChoiceBox.getValue();
    }

    public Attack getSelectedAttack2() {
        return attack2ChoiceBox.getValue();
    }

    public Attack getSelectedAttack3() {
        return attack3ChoiceBox.getValue();
    }

    public String getSelectedStarter() {
        return starterChoiceBox.getValue();
    }
}
