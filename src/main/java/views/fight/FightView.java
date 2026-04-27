package views.fight;

import configuration.AppConfig;
import controllers.Controller;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import models.bugemon.Bugemon;
import models.game.TowerNodeType;
import views.listener.FightListener;
import models.bugemon.dto.BugemonDTO;
import views.View;
import views.utils.SpriteLoader;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    @FXML private FightChoicePanelSubView choicePanelController;
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

    /** Sprite image of the player's active Bugémon. */
    @FXML private ImageView playerSprite;

    /** Sprite image of the enemy's active Bugémon. */
    @FXML private ImageView enemySprite;

    /** 2×3 grid displaying the player's team sprites. */
    @FXML private GridPane teamGrid;

    /** 2×3 grid displaying the enemy's team sprites. */
    @FXML private GridPane enemyGrid;

    @FXML private Label floorInfoLabel;


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
        super(AppConfig.FXML_FIGHT);
    }

    /**
     * Registers the listener that will receive all fight-related user actions.
     * Must be called before any button interaction occurs.
     *
     * @param listener the {@link FightListener} implementation (typically the controller)
     */
    public void setListener(FightListener listener) {
        this.listener = listener;
        choicePanelController.setListener(listener);
    }

    @Override
    protected String getTitle() {
        return "Bugémon - Combat";
    }


    /**
     * Populates the player's or enemy's team grid with sprites for each Bugémon.
     * Player sprites show an HP popup on hover and toggle a stats popup on click.
     *
     * @param team the list of Bugémon DTOs to display
     */
    public void displayTeam(List<BugemonDTO> team) throws IOException {
        boolean  isPlayer = team.getFirst().getIsPlayer();
        GridPane grid     = isPlayer ? teamGrid : enemyGrid;
        grid.getChildren().clear();

        for (int i = 0; i < team.size(); i++) {
            BugemonDTO bugemon = team.get(i);
            ImageView imgView = createSprite(60, bugemon);
            if (isPlayer && bugemon.isDefault()) {
                bindTeamSpriteEvents(imgView, bugemon);
                imgView.setUserData(bugemon);
                imgView.setScaleX(-1);
                if (bugemon.isKO()){
                    imgView.setEffect(new ColorAdjust(0, -1, 0, 0));
                }
            }

            grid.add(imgView, i % 3, i / 3);
        }
    }

    /**
     * Binds mouse events (enter, move, exit, click) to a team sprite ImageView.
     * Shows HP popup on hover and toggles stats popup on click.
     *
     * @param imgView the ImageView to bind events to
     * @param bugemon the Bugémon DTO associated with this sprite
     */
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
        updateBugemonHPAndEffectDisplay(bugemon);

        Label     nameLabel   = bugemon.getIsPlayer() ? playerNameLabel   : enemyNameLabel;
        Label     hpLabel     = bugemon.getIsPlayer() ? playerHpLabel     : enemyHpLabel;
        Label     effectLabel = bugemon.getIsPlayer() ? playerEffectLabel : enemyEffectLabel;
        ImageView sprite      = bugemon.getIsPlayer() ? playerSprite      : enemySprite;
        handleVisibility(bugemon, true);
        sprite.setOpacity(1.0);
        nameLabel.setText(bugemon.getName() + ( bugemon.getIsPlayer() ? " (niveau " +  bugemon.getlevel() + ")" : "") );
        hpLabel.setText("HP: " + bugemon.getcurrenPv() + "/" + bugemon.getPv());
        int atk  = bugemon.getAttackModifier();
        int def  = bugemon.getDefenseModifier();
        int init = bugemon.getInitiativeModifier();
        effectLabel.setText("Effets :"
                + "\n attaque : "    + (atk  > 0 ? "+" + atk  : atk)
                + "\n defense : "    + (def  > 0 ? "+" + def  : def)
                + "\n initiative : " + (init > 0 ? "+" + init : init));
        try {
            Image img = SpriteLoader.loadSprite(bugemon);
            sprite.setImage(img);
        } catch (IOException e) {
            throw new RuntimeException("Sprite introuvable pour " + bugemon.getName(), e);
        }

        if (bugemon.getIsPlayer() && bugemon.isDefault()) {
            sprite.setScaleX(-1);
        } else {
            sprite.setScaleX(1);
        }

        if (bugemon.getIsPlayer()) choicePanelController.updateAttackButtons(bugemon);
        sprite.setVisible(true);
    }

    /**
     * Updates both the HP progress bar and the HP label for the given Bugémon.
     *
     * @param bugemon the Bugémon DTO whose HP to update
     */
    public void updateBugemonHPAndEffectDisplay(BugemonDTO bugemon) {
        ProgressBar hpBar       = bugemon.getIsPlayer() ? playerHpBarFill  : enemyHpBarFill;
        Label       hpLabel     = bugemon.getIsPlayer() ? playerHpLabel    : enemyHpLabel;
        Label       effectLabel = bugemon.getIsPlayer() ? playerEffectLabel : enemyEffectLabel;

        float ratio = Math.round((bugemon.getcurrenPv() / (float) bugemon.getPv()) * 10000f) / 10000f;
        hpBar.setProgress(ratio);
        hpLabel.setText("HP: " + bugemon.getcurrenPv() + "/" + bugemon.getPv());

        int atk  = bugemon.getAttackModifier();
        int def  = bugemon.getDefenseModifier();
        int init = bugemon.getInitiativeModifier();
        effectLabel.setText("Effets :"
                + "\n attaque : "    + (atk  > 0 ? "+" + atk  : atk)
                + "\n defense : "    + (def  > 0 ? "+" + def  : def)
                + "\n initiative : " + (init > 0 ? "+" + init : init));
    }

    /**
     * Updates the combat message label.
     *
     * @param message the message to display
     */
    public void updateCombatMessage(String message) {
        choicePanelController.updateCombatMessage(message);
    }


    /**
     * Updates the cached effectiveness messages used to label attack buttons.
     *
     * @param predictedEffectivenessFromFightModels messages from the fight model
     */
    public void updatePredictedEffectiveness(List<String> predictedEffectivenessFromFightModels) {
        choicePanelController.updatePredictedEffectiveness(predictedEffectivenessFromFightModels);
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
        choicePanelController.showAttackandActionPanels(showAttack);
    }

    /**
     * Enables or disables all main action buttons.
     *
     * @param disabled {@code true} to disable, {@code false} to enable
     */
    public void setButtonsDisabled(boolean disabled) {
        choicePanelController.setButtonsDisabled(disabled);
    }

    /**
     * Resets the entire fight display to its initial blank state.
     * Clears sprites, labels, HP bars, attack buttons, and hides all popups.
     */
    public void resetDisplay() {
        choicePanelController.reset();
        teamGrid.getChildren().clear();
        enemyGrid.getChildren().clear();

        playerSprite.setImage(null);  playerSprite.setVisible(false);
        enemySprite.setImage(null);   enemySprite.setVisible(false);

        playerNameLabel.setText("");  playerHpLabel.setText("");
        playerEffectLabel.setText(""); playerHpBarFill.setProgress(1.0);

        enemyNameLabel.setText("");   enemyHpLabel.setText("");
        enemyEffectLabel.setText(""); enemyHpBarFill.setProgress(1.0);

        popup.hideHp();
        popup.hideStats();
    }


    /**
     * Creates an ImageView sprite from a resource path with the specified size.
     * Preserves the aspect ratio of the image.
     *
     * @param size the width and height to fit the image to
     * @return a configured ImageView with the loaded sprite
     */
    private ImageView createSprite(int size, BugemonDTO bugemon ) throws IOException {
        Image     img  = SpriteLoader.loadSprite(bugemon);
        ImageView view = new ImageView(img);
        view.setFitHeight(size);
        view.setFitWidth(size);
        view.setPreserveRatio(true);
        return view;
    }

    public void updateFloorInfo(int floor, int step, TowerNodeType type, boolean finished) {
        if (finished) {
            floorInfoLabel.setText("Tour terminée !");
            return;
        }
        if (type == TowerNodeType.BOSS) {
            floorInfoLabel.setText("NO" + floor + " - Boss");
            return;
        }
        floorInfoLabel.setText("NO" + floor + " - Étape " + (step + 1) + " / 6");
    }

    public void createStartMessage(String enemyBugemonName, boolean playerCanStart) {
        String message = "L'adversaire envoie " + enemyBugemonName + " ! "
                + (!playerCanStart
                ? "L'adversaire a plus d'initiative, il lance les hostilités !"
                : "L'adversaire a moins d'initiative, vous lancez les hostilités !");
        choicePanelController.updateCombatMessage(message);
    }

    private void handleVisibility(BugemonDTO bugemon, boolean mustBeVisible) {
        if (bugemon.getIsPlayer()) {
            playerSprite.setVisible(mustBeVisible);
            playerHpBarFill.setVisible(mustBeVisible);
            playerHpLabel.setVisible(mustBeVisible);
            playerNameLabel.setVisible(mustBeVisible);
            playerEffectLabel.setVisible(mustBeVisible);
        } else {
            enemySprite.setVisible(mustBeVisible);
            enemyHpBarFill.setVisible(mustBeVisible);
            enemyHpLabel.setVisible(mustBeVisible);
            enemyNameLabel.setVisible(mustBeVisible);
            enemyEffectLabel.setVisible(mustBeVisible);
        }
    }

    public void animateAttack(BugemonDTO bugemon, Runnable afterAttackAnimation) {
        ImageView sprite = bugemon.getIsPlayer() ? playerSprite : enemySprite;
        double direction = bugemon.getIsPlayer() ? 60 : -60;

        TranslateTransition forward = new TranslateTransition(Duration.seconds(0.3), sprite);
        forward.setByX(direction);
        TranslateTransition back = new TranslateTransition(Duration.seconds(0.2), sprite);
        back.setByX(-direction);

        SequentialTransition seq = new SequentialTransition(forward, back);
        seq.setOnFinished(e -> {
            // Ensure sprite is exactly at original position and then run callback
            sprite.setTranslateX(0);
            if (afterAttackAnimation != null) afterAttackAnimation.run();
        });
        seq.play();
    }

    public void animateHit(BugemonDTO bugemon, Runnable afterHitAnimation) {
        ImageView sprite = bugemon.getIsPlayer() ? playerSprite : enemySprite;

        // TODO: créer Timeline qui fait flasher le sprite en rouge/blanc 3x en 0.3s total
        Timeline flash = new Timeline(/* KeyFrames */);
        flash.setOnFinished(e -> afterHitAnimation.run());
        flash.play();
    }

    public void animateKO(BugemonDTO bugemon, Runnable afterKOAnimation) {
        ImageView sprite = bugemon.getIsPlayer() ? playerSprite : enemySprite;

        // TODO: créer FadeTransition opacity 1.0 -> 0.0 sur 0.5s
        FadeTransition fade = new FadeTransition(/* duration, sprite */);
        fade.setOnFinished(e -> {
            handleVisibility(bugemon, false);
            afterKOAnimation.run();
        });
        fade.play();
    }
}