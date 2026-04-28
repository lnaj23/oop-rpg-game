package views.tower;

import configuration.AppConfig;
import controllers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.bugemon.Attack;
import views.View;
import views.listener.SelectAttackListener;

import java.io.IOException;
import java.util.List;

/**
 * View displaying a Bugémon's current attacks so the player
 * can choose which one to replace with a new attack.
 */
public class SelectAttackView extends View<Controller> {

    @FXML private Label titleLabel;
    @FXML private Label newAttackLabel;
    @FXML private VBox attackButtonsBox;
    @FXML private Button btnCancel;

    private SelectAttackListener listener;

    /**
     * Loads the FXML layout for the attack selection sub-window.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public SelectAttackView() throws IOException {
        super("/fxml/tower/selectattackview.fxml");
    }

    @Override
    protected String getTitle() {
        return "Choisir une attaque à remplacer";
    }

    /**
     * Sets the listener for attack selection events.
     *
     * @param listener the listener that handles user interactions
     */
    public void setListener(SelectAttackListener listener) {
        this.listener = listener;
        btnCancel.setOnAction(e -> listener.onCancelClicked());
    }

    /**
     * Displays the current attacks of the selected Bugémon as buttons
     * and shows the new attack that will replace the chosen one.
     *
     * @param attacks       the Bugémon's current attacks
     * @param newAttackName the name of the new attack to learn
     */
    public void displayAttacks(List<Attack> attacks, String newAttackName) {
        attackButtonsBox.getChildren().clear();
        newAttackLabel.setText("Nouvelle attaque : " + newAttackName);

        for (int i = 0; i < attacks.size(); i++) {
            Attack attack = attacks.get(i);
            Button btn = new Button(attack.getName() + " (puissance: " + attack.getPower() + ")");
            btn.setMaxWidth(Double.MAX_VALUE);

            final int index = i;
            btn.setOnAction(e -> listener.onAttackSelectedToReplace(index));

            attackButtonsBox.getChildren().add(btn);
        }
    }
}