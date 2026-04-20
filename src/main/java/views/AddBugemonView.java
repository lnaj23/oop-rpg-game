package views;

import configuration.AppConfig;
import controllers.AddBugemonController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.StageStyle;
import models.bugemon.Attack;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * View associated with the Add Bugemon screen.
 * <p>
 * This view manages the JavaFX components of the form, binds button actions
 * to the controller, fills the choice boxes with the available options, and
 * provides helper methods to read the values entered by the user.
 * </p>
 */
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

    /**
     * Creates the Add Bugemon view and loads its FXML definition.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public AddBugemonView() throws IOException {
        super(AppConfig.FXML_ADDBUGEMON);
    }

    /**
     * Binds the action buttons of the view to the corresponding controller methods.
     */
    public void setActionButtons() {
        btnCancel.setOnAction(e -> this.controller.goToTitleScreen());
        btnAdd.setOnAction(e -> this.controller.addBugemon());
        btnChooseSprite.setOnAction(e -> this.controller.chooseSprite());
        typeChoiceBox.setOnAction(e -> this.controller.onTypeSelected(typeChoiceBox.getValue()));
    }
    /**
     * Fills the type choice box with the available Bugemon types.
     */
    public void setTypeChoiceBox() {

        List<String> types = List.of("Flora", "Pyro", "Aqua", "Litho");
        typeChoiceBox.getItems().setAll(types);

    }

    /**
     * Fills the three attack choice boxes with the provided list of attacks.
     *
     * @param attacks the list of attacks available for selection
     */
    public void setAttackChoiceBox(List<Attack> attacks) {
        attack1ChoiceBox.getSelectionModel().clearSelection();
        attack2ChoiceBox.getSelectionModel().clearSelection();
        attack3ChoiceBox.getSelectionModel().clearSelection();

        attack1ChoiceBox.getItems().setAll(attacks);
        attack2ChoiceBox.getItems().setAll(attacks);
        attack3ChoiceBox.getItems().setAll(attacks);
    }

    /**
     * Enables or disables the three attack choice boxes.
     *
     * @param disabled {@code true} to disable the attack choice boxes,
     *                 {@code false} to enable them
     */
    public void setAttackChoiceBoxesDisabled(boolean disabled) {
        attack1ChoiceBox.setDisable(disabled);
        attack2ChoiceBox.setDisable(disabled);
        attack3ChoiceBox.setDisable(disabled);
    }

    /**
     * Fills the starter choice box with the available yes/no values.
     */
    public void setStarterChoiceBox() {
        List<String> yesno = List.of("Oui", "Non");
        starterChoiceBox.getItems().setAll(yesno);
    }

    /**
     * Updates the sprite path text field with the given file path.
     *
     * @param path the selected sprite file path
     */
    public void setSpritePath(String path) {
        spritePathField.setText(path);
    }

    /**
     * Displays an alert dialog with the given message.
     *
     * @param message the message to display in the alert
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

    /**
     * Returns the browse button used to choose a sprite file.
     *
     * @return the sprite chooser button
     */
    public Button getBtnChooseSprite() {
        return btnChooseSprite;
    }

    /**
     * Returns the title of this view.
     *
     * @return the window title
     */
    @Override
    public String getTitle() {
        return "Add Bugemon";
    }

    /**
     * Returns the value entered in the name field.
     *
     * @return the Bugemon name
     */
    public String getNameValue() {
        return nameField.getText();
    }

    /**
     * Returns the value entered in the HP field.
     *
     * @return the HP text value
     */
    public String getHpValue() {
        return hpField.getText();
    }

    /**
     * Returns the value entered in the attack field.
     *
     * @return the attack stat text value
     */
    public String getAttackValue() {
        return attackField.getText();
    }

    /**
     * Returns the value entered in the defense field.
     *
     * @return the defense stat text value
     */
    public String getDefenseValue() {
        return defenseField.getText();
    }

    /**
     * Returns the selected sprite file path.
     *
     * @return the sprite path
     */
    public String getSpritePathValue() {
        return spritePathField.getText();
    }

    /**
     * Returns the selected Bugemon type.
     *
     * @return the selected type
     */
    public String getSelectedType() {
        return typeChoiceBox.getValue();
    }

    /**
     * Returns the first selected attack.
     *
     * @return the selected first attack
     */
    public Attack getSelectedAttack1() {
        return attack1ChoiceBox.getValue();
    }

    /**
     * Returns the second selected attack.
     *
     * @return the selected second attack
     */
    public Attack getSelectedAttack2() {
        return attack2ChoiceBox.getValue();
    }

    /**
     * Returns the third selected attack.
     *
     * @return the selected third attack
     */
    public Attack getSelectedAttack3() {
        return attack3ChoiceBox.getValue();
    }

    /**
     * Returns the selected starter value.
     *
     * @return the selected starter value
     */
    public String getSelectedStarter() {
        return starterChoiceBox.getValue();
    }
}