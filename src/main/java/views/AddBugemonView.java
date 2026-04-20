package views;

import configuration.AppConfig;
import controllers.AddBugemonController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.StageStyle;
import models.bugemon.Attack;
import services.dataprovider.AttackProvider;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    /**
     * Displays an error alert with the given title and message.
     *
     * @param message the alert message
     */
    public void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(AppConfig.CSS_COMMON)).toExternalForm()
        );
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setGraphic(null);
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
