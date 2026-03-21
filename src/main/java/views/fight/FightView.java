package views.fight;

import controllers.Controller;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import listener.FightListener;
import models.bugemon.dto.BugemonDTO;
import views.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * View for the fight screen.
 * <p>
 * Displays the battle UI: Bugémon sprites, HP bars, combat messages,
 * action/attack panels, and the player's team grid.
 * Handles mouse hover and click popups on team sprites via the inherited {@link #popup} helper.
 * All user interactions are forwarded to a {@link FightListener};
 * this view has no direct dependency on any concrete controller.
 * </p>
 */
public class FightView extends View<Controller> {

    /**
     * The listener that receives all fight-related user actions.
     * Set via {@link #setListener(FightListener)}.
     */
    private FightListener listener;

    /** Label displaying the enemy Bugémon's name. */
    @FXML private Label enemyNameLabel;

    /** Label displaying the enemy Bugémon's current HP. */
    @FXML private Label enemyHpLabel;

    /** Label displaying the enemy Bugémon's active status effects. */
    @FXML private Label enemyEffectLabel;

    /** Progress bar representing the enemy Bugémon's HP ratio. */
    @FXML private ProgressBar enemyHpBarFill;

    /** Label displaying the player Bugémon's name. */
    @FXML private Label playerNameLabel;

    /** Label displaying the player Bugémon's current HP. */
    @FXML private Label playerHpLabel;

    /** Label displaying the player Bugémon's active status effects. */
    @FXML private Label playerEffectLabel;

    /** Progress bar representing the player Bugémon's HP ratio. */
    @FXML private ProgressBar playerHpBarFill;

    /** Label displaying the latest combat event message. */
    @FXML private Label combatMessageLabel;

    /** Panel containing the four main action buttons (Attack, Item, Switch, Forfeit). */
    @FXML private GridPane actionPanel;

    /** Button to open the attack selection panel. */
    @FXML private Button btnAttack;

    /** Button to use an item. */
    @FXML private Button btnItem;

    /** Button to switch the active Bugémon. */
    @FXML private Button btnSwitch;

    /** Button to forfeit the battle and return to the title screen. */
    @FXML private Button btnForfeit;

    /** Panel containing the attack buttons and the back button. */
    @FXML private GridPane attackPanel;

    /** Button to use the first attack. */
    @FXML private Button btnAttack1;

    /** Button to use the second attack. */
    @FXML private Button btnAttack2;

    /** Button to use the third attack. */
    @FXML private Button btnAttack3;

    /** Button to go back from the attack panel to the action panel. */
    @FXML private Button btnBackFromAttack;

    /** Sprite image of the player's active Bugémon. */
    @FXML private ImageView playerSprite;

    /** Sprite image of the enemy's active Bugémon. */
    @FXML private ImageView enemySprite;

    /** 2×3 grid displaying the player's team sprites. */
    @FXML private GridPane teamGrid;

    /** 2×3 grid displaying the enemy's team sprites. */
    @FXML private GridPane enemyGrid;

    /** Effectiveness message per attack slot, updated by {@link #updatePredictedEffectiveness(List)}. */
    private List<String> predictedEffectiveness = new ArrayList<>();

    /**
     * Constructs the view by loading the FXML and binding all button actions.
     * <p>
     * Note: button handlers reference {@link #listener} via lambda — the listener
     * must be set with {@link #setListener(FightListener)} before any button is clicked.
     * </p>
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public FightView() throws IOException {
        super("/fxml/fight/fightview.fxml");

        btnAttack.setOnAction(e -> showAttackandActionPanels(true));
        btnItem.setOnAction(e -> this.listener.onItemClicked());
        btnSwitch.setOnAction(e -> this.listener.onSwitchDisplaeyPlayerClicked());
        btnForfeit.setOnAction(e -> this.listener.onForfeitClicked());

        btnAttack1.setOnAction(e -> this.listener.onAttackSelected(0));
        btnAttack2.setOnAction(e -> this.listener.onAttackSelected(1));
        btnAttack3.setOnAction(e -> this.listener.onAttackSelected(2));
        btnBackFromAttack.setOnAction(e -> showAttackandActionPanels(false));
    }

    /**
     * Registers the listener that will receive all fight-related user actions.
     * Must be called before any button interaction occurs.
     *
     * @param listener the {@link FightListener} implementation (typically the controller)
     */
    public void setListener(FightListener listener) {
        this.listener = listener;
    }

    /**
     * Shows a non-blocking alert, then calls {@code onClose} when the user dismisses it.
     *
     * @param title   the alert title
     * @param message the alert body
     * @param onClose callback invoked when the dialog is hidden
     */
    public void showMessageReturnTitle(String title, String message, Runnable onClose) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/common.css")).toExternalForm()
        );
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setGraphic(null);
        alert.setOnHidden(e -> onClose.run());
        alert.show();
    }

    @Override
    protected String getTitle() {
        return "Bugémon – Combat";
    }


    /**
     * Populates the player's or enemy's team grid with sprites for each Bugémon.
     * Player sprites show an HP popup on hover and toggle a stats popup on click.
     *
     * @param team the list of Bugémon DTOs to display
     */
    public void displayTeam(List<BugemonDTO> team) {
        boolean  isPlayer = team.get(0).getIsPlayer();
        GridPane grid     = isPlayer ? teamGrid : enemyGrid;
        grid.getChildren().clear();

        for (int i = 0; i < team.size(); i++) {
            BugemonDTO bugemon = team.get(i);
            String     path    = isPlayer
                    ? "/Assets/png/" + bugemon.getSprite()
                    : "/Assets/png/nobugemon/ulb.png";

            ImageView imgView = createSprite(path, 60);

            if (isPlayer) {
                bindTeamSpriteEvents(imgView, bugemon);
                imgView.setUserData(bugemon);
            }

            grid.add(imgView, i % 3, i / 3);
        }
    }

    private void bindTeamSpriteEvents(ImageView imgView, BugemonDTO bugemon) {
        imgView.setOnMouseEntered(e -> showHpPopup(e, bugemon, imgView));
        imgView.setOnMouseMoved(this::updatePopupPosition);
        imgView.setOnMouseExited(e -> HPPopup.hide());
        imgView.setOnMouseClicked(e -> {
            if (popup.isStatsShowing()) {
                popup.hideStats();
                showHpPopup(e, bugemon, imgView);
            } else {
                HPPopup.hide();
                showStatsPopup(e.getScreenX(), e.getScreenY(), bugemon, imgView);
            }
        });
    }


    /**
     * Updates the sprite, name, HP, effect label, and (for the player) attack buttons
     * for the given Bugémon.
     *
     * @param bugemon the Bugémon DTO to display
     */
    public void updateActiveBugemonSprite(BugemonDTO bugemon) {
        updateBugemonHPDisplay(bugemon);

        Label     nameLabel   = bugemon.getIsPlayer() ? playerNameLabel   : enemyNameLabel;
        Label     hpLabel     = bugemon.getIsPlayer() ? playerHpLabel     : enemyHpLabel;
        Label     effectLabel = bugemon.getIsPlayer() ? playerEffectLabel : enemyEffectLabel;
        ImageView sprite      = bugemon.getIsPlayer() ? playerSprite      : enemySprite;

        nameLabel.setText(bugemon.getName());
        hpLabel.setText("HP: " + bugemon.getcurrenPv() + "/" + bugemon.getPv());
        effectLabel.setText("Effets : Aucun");

        Image img = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/Assets/png/" + bugemon.getSprite())));
        sprite.setImage(img);

        if (bugemon.getIsPlayer()) updateAttackButtons(bugemon);
        sprite.setVisible(true);
    }

    /**
     * Updates both the HP progress bar and the HP label for the given Bugémon.
     *
     * @param bugemon the Bugémon DTO whose HP to update
     */
    public void updateBugemonHPDisplay(BugemonDTO bugemon) {
        ProgressBar hpBar       = bugemon.getIsPlayer() ? playerHpBarFill  : enemyHpBarFill;
        Label       hpLabel     = bugemon.getIsPlayer() ? playerHpLabel    : enemyHpLabel;
        Label       effectLabel = bugemon.getIsPlayer() ? playerEffectLabel : enemyEffectLabel;

        float ratio = Math.round((bugemon.getcurrenPv() / (float) bugemon.getPv()) * 10000f) / 10000f;
        hpBar.setProgress(ratio);
        hpLabel.setText("HP: " + bugemon.getcurrenPv() + "/" + bugemon.getPv());

        List<String> effects = bugemon.getActifEffectNames();
        effectLabel.setText("Effets : " + (effects.isEmpty() ? "Aucun" : String.join(", ", effects)));
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
     * Updates the cached effectiveness messages used to label attack buttons.
     *
     * @param predictedEffectivenessFromFightModels messages from the fight model
     */
    public void updatePredictedEffectiveness(List<String> predictedEffectivenessFromFightModels) {
        this.predictedEffectiveness = predictedEffectivenessFromFightModels;
    }

    private void updateAttackButtons(BugemonDTO bugemon) {
        List<String> attackNames = formatAttackNames(bugemon.getAttackNamesAsList());
        List<Button> btns        = List.of(btnAttack1, btnAttack2, btnAttack3);

        for (int i = 0; i < btns.size(); i++) {
            Button btn = btns.get(i);
            if (i < attackNames.size()) {
                String typeColor   = bugemon.getAttackColor(i);
                String borderColor = getEffectivenessColor(predictedEffectiveness.get(i));
                btn.setText(attackNames.get(i) + "\n" + predictedEffectiveness.get(i));
                btn.setStyle(
                        "-fx-background-color: " + typeColor    + "; " +
                                "-fx-text-fill: white; "                        +
                                "-fx-background-radius: 5; "                    +
                                "-fx-border-color: "     + borderColor  + "; " +
                                "-fx-border-width: 5; "                         +
                                "-fx-border-radius: 5;"
                );
                btn.setVisible(true);
            } else {
                btn.setVisible(false);
            }
        }
    }

    private String getEffectivenessColor(String effectiveness) {
        return switch (effectiveness) {
            case "Super efficace !" -> "rgb(0,152,0)";
            case "Peu efficace..."  -> "#FF0000";
            default                 -> "transparent";
        };
    }


    /**
     * Applies a greyscale effect to the team grid sprite of the given fainted Bugémon.
     *
     * @param bugemon the fainted Bugémon DTO
     */
    public void markBugemonAsFainted(BugemonDTO bugemon) {
        GridPane grid = bugemon.getIsPlayer() ? teamGrid : enemyGrid;
        for (Node node : grid.getChildren()) {
            if (bugemon.getIsPlayer()) {
                if (node instanceof ImageView iv &&
                        Objects.equals(bugemon.getId(), ((BugemonDTO) iv.getUserData()).getId())) {
                    iv.setEffect(new ColorAdjust(0, -1, 0, 0));
                    break;
                }
            } else {
                if (!(node.getEffect() instanceof ColorAdjust ca && ca.getSaturation() == -1.0)) {
                    node.setEffect(new ColorAdjust(0, -1, 0, 0));
                    break;
                }
            }
        }
    }

    /**
     * Removes the greyscale effect from the team grid sprite of the given Bugémon.
     * For the enemy grid, targets the last greyed-out sprite.
     *
     * @param bugemon the Bugémon DTO to unmark
     */
    public void unMarkBugemonAsFainted(BugemonDTO bugemon) {
        GridPane grid = bugemon.getIsPlayer() ? teamGrid : enemyGrid;
        if (bugemon.getIsPlayer()) {
            for (Node node : grid.getChildren()) {
                if (node instanceof ImageView iv &&
                        Objects.equals(bugemon.getId(), ((BugemonDTO) iv.getUserData()).getId())) {
                    iv.setEffect(null);
                    break;
                }
            }
        } else {
            List<Node> children = grid.getChildren();
            for (int i = children.size() - 1; i >= 0; i--) {
                Node node = children.get(i);
                if (node.getEffect() instanceof ColorAdjust ca && ca.getSaturation() == -1.0) {
                    node.setEffect(null);
                    break;
                }
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
     * Resets the entire fight display to its initial blank state.
     * Clears sprites, labels, HP bars, attack buttons, and hides all popups.
     */
    public void resetDisplay() {
        showAttackandActionPanels(false);
        setButtonsDisabled(false);

        teamGrid.getChildren().clear();
        enemyGrid.getChildren().clear();

        playerSprite.setImage(null);  playerSprite.setVisible(false);
        enemySprite.setImage(null);   enemySprite.setVisible(false);

        playerNameLabel.setText("");  playerHpLabel.setText("");
        playerEffectLabel.setText(""); playerHpBarFill.setProgress(1.0);

        enemyNameLabel.setText("");   enemyHpLabel.setText("");
        enemyEffectLabel.setText(""); enemyHpBarFill.setProgress(1.0);

        combatMessageLabel.setText("");

        List.of(btnAttack1, btnAttack2, btnAttack3).forEach(btn -> {
            btn.setText("");
            btn.setVisible(false);
        });

        popup.hideHp();
        popup.hideStats();
    }


    private ImageView createSprite(String resourcePath, int size) {
        Image     img  = new Image(Objects.requireNonNull(getClass().getResourceAsStream(resourcePath)));
        ImageView view = new ImageView(img);
        view.setFitHeight(size);
        view.setFitWidth(size);
        view.setPreserveRatio(true);
        return view;
    }

    /**
     * Formats raw snake_case attack names into capitalised display names.
     * Example: {@code "fire_blast"} → {@code "Fire Blast"}.
     */
    private List<String> formatAttackNames(List<String> raw) {
        return raw.stream()
                .map(s -> Arrays.stream(s.split("_"))
                        .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                        .collect(Collectors.joining(" ")))
                .toList();
    }
}