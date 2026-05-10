package views.tower;

import controllers.tower.SelectAttackController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import models.bugemon.Attack;
import views.View;

import java.io.IOException;
import java.util.List;

/**
 * View displaying a Bugémon's current attacks so the player
 * can choose which one to replace with a new attack.
 * <p>
 * Bound 1:1 to {@link SelectAttackController} — the buttons forward their
 * actions directly to the controller (no listener layer).
 * </p>
 */
public class SelectAttackView extends View {

    private SelectAttackController controller;

    /**
     * Sets the controller used by this view to delegate user actions.
     *
     * @param controller the controller associated with this view
     */
    public void setController(SelectAttackController controller) {
        this.controller = controller;
    }

    @FXML private Label titleLabel;
    @FXML private Label newAttackLabel;
    @FXML private VBox attackButtonsBox;
    @FXML private Button btnCancel;

    /**
     * Loads the FXML layout for the attack selection sub-window and wires
     * the cancel button to the controller.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public SelectAttackView() throws IOException {
        super("/fxml/tower/selectattackview.fxml");
        btnCancel.setOnAction(e -> this.controller.onCancelClicked());
    }

    @Override
    protected String getTitle() {
        return "Choisir une attaque à remplacer";
    }

    /**
     * Displays the current attacks of the selected Bugémon as buttons
     * and shows the new attack that will replace the chosen one.
     * <p>
     * Must be called after {@link #setController(SelectAttackController)}.
     * </p>
     */
    public void displayAttacks(List<Attack> attacks, String newAttackName) {
        attackButtonsBox.getChildren().clear();
        newAttackLabel.setText("Nouvelle attaque : " + newAttackName);

        for (int i = 0; i < attacks.size(); i++) {
            Attack attack = attacks.get(i);
            Button btn = new Button(attack.getName() + " (puissance: " + attack.getPower() + ")");
            btn.setMaxWidth(Double.MAX_VALUE);

            final int index = i;
            btn.setOnAction(e -> this.controller.onAttackSelectedToReplace(index));

            attackButtonsBox.getChildren().add(btn);
        }
    }
}