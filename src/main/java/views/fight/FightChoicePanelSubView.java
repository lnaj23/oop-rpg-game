package views.fight;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import views.listener.FightListener;
import models.bugemon.dto.BugemonDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the fight choice panel in JavaFX.
 * <p>
 * Manages the display and interaction of action buttons (Attack, Item, Switch, Forfeit)
 * and attack selection buttons. Displays combat messages and updates attack button styling
 * based on type colors and effectiveness predictions.
 * All user interactions are forwarded to a {@link FightListener}.
 * </p>
 */
public class FightChoicePanelSubView {

    /** The listener that receives fight-related user actions. */
    private FightListener listener;

    /** Cached list of effectiveness messages for the current Bugémon's attacks. */
    private List<String> predictedEffectiveness = new ArrayList<>();

    /** Label displaying combat feedback messages. */
    @FXML private Label combatMessageLabel;

    /** Grid panel containing main action buttons. */
    @FXML private GridPane actionPanel;
    /** Button to open the attack selection panel. */
    @FXML private Button btnAttack;
    /** Button to trigger item selection. */
    @FXML private Button btnItem;
    /** Button to switch the active Bugémon. */
    @FXML private Button btnSwitch;
    /** Button to forfeit the battle. */
    @FXML private Button btnForfeit;

    /** Grid panel containing attack selection buttons. */
    @FXML private GridPane attackPanel;
    /** Button to select the first attack. */
    @FXML private Button btnAttack1;
    /** Button to select the second attack. */
    @FXML private Button btnAttack2;
    /** Button to select the third attack. */
    @FXML private Button btnAttack3;
    /** Button to return from attack selection to the main action panel. */
    @FXML private Button btnBackFromAttack;

    /**
     * Initializes all button event handlers.
     * <p>
     * Binds each button to call the corresponding listener method.
     * Note: {@link #listener} must be set before any button is clicked.
     * </p>
     */
    @FXML
    public void initialize() {
        btnAttack.setOnAction(e -> showAttackandActionPanels(true));
        btnItem.setOnAction(e -> {
            if (listener != null) listener.onItemClicked();
        });
        btnSwitch.setOnAction(e -> {
            if (listener != null) listener.onSwitchDisplaeyPlayerClicked();
        });
        btnForfeit.setOnAction(e -> {
            if (listener != null) listener.onForfeitClicked();
        });

        btnAttack1.setOnAction(e -> {
            if (listener != null) listener.onAttackSelected(0);
        });
        btnAttack2.setOnAction(e -> {
            if (listener != null) listener.onAttackSelected(1);
        });
        btnAttack3.setOnAction(e -> {
            if (listener != null) listener.onAttackSelected(2);
        });

        btnBackFromAttack.setOnAction(e -> showAttackandActionPanels(false));
    }

    /**
     * Registers the listener that will receive all fight choice panel user actions.
     * Must be called before any button interaction occurs.
     *
     * @param listener the {@link FightListener} implementation (typically the controller)
     */
    public void setListener(FightListener listener) {
        this.listener = listener;
    }

    /**
     * Updates the combat message label.
     *
     * @param message the message to display
     */
    public void updateCombatMessage(String message) {
        combatMessageLabel.setText(message);
    }

    /**
     * Caches the effectiveness messages for the current Bugémon's attacks.
     * These messages are used to style attack buttons.
     *
     * @param predictedEffectivenessFromFightModels list of effectiveness strings
     *        (e.g., "Super efficace !", "Peu efficace...", or neutral)
     */
    public void updatePredictedEffectiveness(List<String> predictedEffectivenessFromFightModels) {
        this.predictedEffectiveness = predictedEffectivenessFromFightModels;
    }

    /**
     * Updates all three attack buttons with the given Bugémon's attack names,
     * effectiveness predictions, and type colors.
     * <p>
     * Formats attack names by capitalizing each word (e.g., "fire_punch" → "Fire Punch")
     * and styles buttons with the attack type color and an effectiveness border color.
     * </p>
     *
     * @param bugemon the Bugémon DTO containing attack and type information
     */
    public void updateAttackButtons(BugemonDTO bugemon) {
        List<String> attackNames = formatAttackNames(bugemon.getAttackNamesAsList());
        List<Button> btns = List.of(btnAttack1, btnAttack2, btnAttack3);

        for (int i = 0; i < btns.size(); i++) {
            Button btn = btns.get(i);
            if (i < attackNames.size()) {
                String typeColor = bugemon.getAttackColor(i);
                String borderColor = getEffectivenessColor(predictedEffectiveness.get(i));
                btn.setText(attackNames.get(i) + "\n" + predictedEffectiveness.get(i));
                btn.setStyle(
                        "-fx-background-color: " + typeColor + "; " +
                                "-fx-text-fill: white; " +
                                "-fx-background-radius: 5; " +
                                "-fx-border-color: " + borderColor + "; " +
                                "-fx-border-width: 5; " +
                                "-fx-border-radius: 5;"
                );
                btn.setVisible(true);
            } else {
                btn.setVisible(false);
            }
        }
    }

    /**
     * Toggles between the attack panel and the action panel.
     *
     * @param showAttack {@code true} to show the attack panel, {@code false} to show the action panel
     */
    public void showAttackandActionPanels(boolean showAttack) {
        attackPanel.setVisible(showAttack);
        attackPanel.setManaged(showAttack);
        actionPanel.setVisible(!showAttack);
        actionPanel.setManaged(!showAttack);
    }

    /**
     * Enables or disables all main action buttons.
     *
     * @param disabled {@code true} to disable, {@code false} to enable
     */
    public void setButtonsDisabled(boolean disabled) {
        btnAttack.setDisable(disabled);
        btnItem.setDisable(disabled);
        btnSwitch.setDisable(disabled);
        btnForfeit.setDisable(disabled);
    }

    /**
     * Resets the choice panel to its initial state.
     * Shows the action panel, enables all buttons, clears the combat message,
     * and hides attack selection buttons.
     */
    public void reset() {
        showAttackandActionPanels(false);
        setButtonsDisabled(false);
        combatMessageLabel.setText("");

        List.of(btnAttack1, btnAttack2, btnAttack3).forEach(btn -> {
            btn.setText("");
            btn.setVisible(false);
        });
    }

    /**
     * Maps an effectiveness string to its corresponding CSS border color.
     *
     * @param effectiveness the effectiveness message (e.g., "Super efficace !", "Peu efficace...")
     * @return an RGB or hex color string; transparent if neutral
     */
    private String getEffectivenessColor(String effectiveness) {
        return switch (effectiveness) {
            case "Super efficace !" -> "rgb(0,152,0)";
            case "Peu efficace..." -> "#FF0000";
            default -> "transparent";
        };
    }

    /**
     * Formats attack names by capitalizing the first letter of each word.
     * Splits underscore-separated names and joins them with spaces.
     *
     * @param raw list of attack names (e.g., "fire_punch", "ice_beam")
     * @return list of formatted attack names (e.g., "Fire Punch", "Ice Beam")
     */
    private List<String> formatAttackNames(List<String> raw) {
        return raw.stream()
                .map(s -> Arrays.stream(s.split("_"))
                        .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                        .collect(Collectors.joining(" ")))
                .toList();
    }
}
