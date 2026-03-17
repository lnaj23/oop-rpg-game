package views;

import controllers.FightController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.Arrays;
import java.util.List;

import models.bugemon.dto.BugemonDTO;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.stage.Popup;
import javafx.scene.input.MouseEvent;
import service.fight.TypeEffectivenessService;

/**
 * View for the fight screen.
 * <p>
 * Displays the battle UI: Bugémon sprites, HP bars, combat messages,
 * action/attack panels, and the player's team grid.
 * Handles mouse hover and click popups on team sprites.
 * All visual updates are triggered by {@link FightController}.
 * </p>
 */
public class FightView extends View<FightController> {

    /** Popup shown on mouse hover over a team sprite, displaying current HP. */
    private final Popup HPPopup = new Popup();

    /** Label inside {@link #HPPopup} displaying the HP text. */
    private final Label HPLabel = new Label();

    /** Popup shown on click over a team sprite, displaying full stats. */
    private final Popup statsPopup = new Popup();

    /** Label inside {@link #statsPopup} displaying the stats text. */
    private final Label statsLabel = new Label();

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

    /**
     * Constructs the view by loading the FXML and binding all button actions.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public FightView() throws IOException {
        super("/fxml/fightview.fxml");
        initPopup(HPPopup, HPLabel);
        initPopup(statsPopup, statsLabel);

        btnAttack.setOnAction(e -> showAttackandActionPanels(true));
        btnItem.setOnAction(e -> this.controller.onItemClicked());
        btnSwitch.setOnAction(e -> this.controller.onSwitchDisplaeyPlayerClicked());
        btnForfeit.setOnAction(e -> this.controller.onForfeitClicked());

        btnAttack1.setOnAction(e -> this.controller.onAttackSelected(0));
        btnAttack2.setOnAction(e -> this.controller.onAttackSelected(1));
        btnAttack3.setOnAction(e -> this.controller.onAttackSelected(2));
        btnBackFromAttack.setOnAction(e -> showAttackandActionPanels(false));
    }

    /**
     * Returns the title of the fight scene.
     *
     * @return the window title
     */
    @Override
    protected String getTitle() {
        return "Bugémon – Combat";
    }

    /**
     * Populates the player's or enemy's team grid with sprites for each Bugémon in the given list.
     * If the Bugémon belongs to the player, each sprite shows an HP popup on hover and a stats popup on click.
     *
     * @param team the list of Bugémon DTOs to display
     */
    public void displayTeam(List<BugemonDTO> team) {
        boolean isPlayer = team.get(0).getIsPlayer();
        GridPane grid = isPlayer ? teamGrid : enemyGrid;
        grid.getChildren().clear();

        for (int i = 0; i < team.size(); i++) {
            BugemonDTO bugemon = team.get(i);

            String path = isPlayer ? "/Assets/png/" + bugemon.getSprite() : "/Assets/png/nobugemon/ulb.png";
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));

            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(60);
            imgView.setFitWidth(60);
            imgView.setPreserveRatio(true);

            if (isPlayer) {
                imgView.setOnMouseEntered(e -> showHpPopup(e, bugemon, imgView));
                imgView.setOnMouseMoved(this::updatePopupPosition);
                imgView.setOnMouseExited(e -> HPPopup.hide());
                imgView.setOnMouseClicked(e -> {
                    if (statsPopup.isShowing()) {
                        statsPopup.hide();
                        showHpPopup(e, bugemon, imgView);
                    } else {
                        HPPopup.hide();
                        showStatsPopup(e.getScreenX(), e.getScreenY(), bugemon, imgView);
                    }
                });
                imgView.setUserData(bugemon);
            }

            grid.add(imgView, i % 3, i / 3);
        }
    }

    /**
     * Updates the sprite, name, HP, effect label, and attack buttons
     * for the given Bugémon (player or enemy depending on {@code bugemon.getIsPlayer()}).
     *
     * @param bugemon the Bugémon DTO to display
     */
    public void updateActiveBugemonSprite(BugemonDTO bugemon) {
        updateBugemonHPDisplay(bugemon);

        Label nameLabel   = bugemon.getIsPlayer() ? playerNameLabel   : enemyNameLabel;
        Label hpLabel     = bugemon.getIsPlayer() ? playerHpLabel     : enemyHpLabel;
        Label effectLabel = bugemon.getIsPlayer() ? playerEffectLabel : enemyEffectLabel;
        ImageView sprite  = bugemon.getIsPlayer() ? playerSprite      : enemySprite;

        nameLabel.setText(bugemon.getName());
        hpLabel.setText("HP: " + bugemon.getcurrenPv() + "/" + bugemon.getPv());
        effectLabel.setText("Effets : Aucun");

        String path = "/Assets/png/" + bugemon.getSprite();
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        sprite.setImage(img);

        if (bugemon.getIsPlayer()) {
            updateAttackButtonName(bugemon);
        }
        sprite.setVisible(true);
    }

    /**
     * Updates both the HP progress bar and the HP label for the given Bugémon.
     *
     * @param bugemon the Bugémon DTO whose HP to update
     */
    public void updateBugemonHPDisplay(BugemonDTO bugemon) {
        ProgressBar hpBar = bugemon.getIsPlayer() ? playerHpBarFill : enemyHpBarFill;
        Label hpLabel     = bugemon.getIsPlayer() ? playerHpLabel   : enemyHpLabel;

        float maxHP = bugemon.getPv();
        float ratio = Math.round((bugemon.getcurrenPv() / maxHP) * 10000f) / 10000f;
        hpBar.setProgress(ratio);
        hpLabel.setText("HP: " + bugemon.getcurrenPv() + "/" + bugemon.getPv());
    }

    /**
     * Updates the combat message label with the given text.
     *
     * @param message the message to display
     */
    public void updateCombatMessage(String message) {
        combatMessageLabel.setText(message);
    }

    /**
     * Updates the attack button labels to match the attacks of the given Bugémon.
     * Hides buttons for which no attack exists.
     *
     * @param bugemon the Bugémon whose attacks to display
     */
    public void updateAttackButtonName(BugemonDTO bugemon) {
        List<String> attackname = formatAttackNames(bugemon.getAttackNamesAsList());
        List<Button> btns = List.of(btnAttack1, btnAttack2, btnAttack3);

        for (int i = 0; i < btns.size(); i++) {
            if (i < attackname.size()) {
                btns.get(i).setText(attackname.get(i));
                btns.get(i).setVisible(true);
                Button btn = btns.get(i);
                String attackType = bugemon.getAttacks().get(i).getType();
                String typeColor = TypeEffectivenessService.getTypeColorofaAttack(attackType);
                btn.setStyle(
                        "-fx-background-color: " + typeColor + "; " +
                                "-fx-text-fill: white; "                    +
                                "-fx-background-radius: 5;"
                );
            } else {
                btns.get(i).setVisible(false);
            }
        }
    }

    /**
     * Applies a greyscale effect to the team grid sprite matching the given Bugémon,
     * visually indicating it has fainted.
     *
     * @param bugemon the fainted Bugémon DTO
     */
    public void markBugemonAsFainted(BugemonDTO bugemon) {
        GridPane grid = bugemon.getIsPlayer() ? teamGrid : enemyGrid;

        for (Node node : grid.getChildren()) {
            if (bugemon.getIsPlayer()) {
                if (node instanceof ImageView imgView &&
                        Objects.equals(bugemon.getId(), ((BugemonDTO) imgView.getUserData()).getId())) {
                    imgView.setEffect(new ColorAdjust(0, -1, 0, 0));
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
     * Removes the greyscale effect from the team grid sprite matching the given Bugémon.
     * For the enemy grid, removes the effect from the last marked sprite.
     *
     * @param bugemon the Bugémon DTO to unmark
     */
    public void unMarkBugemonAsFainted(BugemonDTO bugemon) {
        GridPane grid = bugemon.getIsPlayer() ? teamGrid : enemyGrid;

        if (bugemon.getIsPlayer()) {
            for (Node node : grid.getChildren()) {
                if (node instanceof ImageView imgView &&
                        Objects.equals(bugemon.getId(), ((BugemonDTO) imgView.getUserData()).getId())) {
                    imgView.setEffect(null);
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
     * @param disabled {@code true} to disable the buttons, {@code false} to enable them
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

        playerSprite.setImage(null);
        playerSprite.setVisible(false);
        enemySprite.setImage(null);
        enemySprite.setVisible(false);

        playerNameLabel.setText("");
        playerHpLabel.setText("");
        playerEffectLabel.setText("");
        playerHpBarFill.setProgress(1.0);

        enemyNameLabel.setText("");
        enemyHpLabel.setText("");
        enemyEffectLabel.setText("");
        enemyHpBarFill.setProgress(1.0);

        combatMessageLabel.setText("");

        List<Button> attackBtns = List.of(btnAttack1, btnAttack2, btnAttack3);
        for (Button btn : attackBtns) {
            btn.setText("");
            btn.setVisible(false);
        }

        HPPopup.hide();
        statsPopup.hide();
    }

    /**
     * Initialises a popup with the given label, applying a dark tooltip style.
     *
     * @param popup the popup to initialise
     * @param label the label to add inside the popup
     */
    private void initPopup(Popup popup, Label label) {
        label.setMouseTransparent(true);
        label.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 5;");
        popup.getContent().add(label);
        popup.setAutoHide(true);
    }

    /**
     * Shows a popup with the full stats of the given Bugémon at the given screen position.
     *
     * @param x       the screen X position
     * @param y       the screen Y position
     * @param bugemon the Bugémon whose stats to display
     * @param node    the node to anchor the popup to
     */
    private void showStatsPopup(double x, double y, BugemonDTO bugemon, ImageView node) {
        String text = "PV: " + bugemon.getPv() + "\n" +
                "ATK: " + bugemon.getAttaque() + "\n" +
                "DEF: " + bugemon.getDefense() + "\n" +
                "INIT: " + bugemon.getInitiative() + "\n" +
                "Attaques: " + bugemon.getAttackNamesAsString();
        statsLabel.setText(text);
        statsPopup.show(node, x + 15, y + 15);
    }

    /**
     * Shows a popup with the current and maximum HP of the given Bugémon.
     *
     * @param event   the mouse event providing the screen position
     * @param bugemon the Bugémon whose HP to display
     * @param node    the node to anchor the popup to
     */
    private void showHpPopup(MouseEvent event, BugemonDTO bugemon, ImageView node) {
        String text = "HP : " + bugemon.getcurrenPv() + " / " + bugemon.getPv();
        HPLabel.setText(text);
        HPPopup.show(node, event.getScreenX() + 15, event.getScreenY() + 15);
    }


    /**
     * Updates the position of the HP popup to follow the mouse cursor.
     *
     * @param event the mouse move event
     */
    private void updatePopupPosition(MouseEvent event) {
        HPPopup.setX(event.getScreenX() + 15);
        HPPopup.setY(event.getScreenY() + 15);
    }

    /**
     * Formats a list of raw snake_case attack names into capitalised display names.
     * Example: {@code "fire_blast"} → {@code "Fire Blast"}.
     *
     * @param raw the list of raw attack name strings
     * @return the list of formatted attack name strings
     */
    private List<String> formatAttackNames(List<String> raw) {
        return raw.stream()
                .map(s -> Arrays.stream(s.split("_"))
                        .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                        .collect(Collectors.joining(" ")))
                .toList();
    }
}