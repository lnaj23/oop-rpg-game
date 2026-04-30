package views.fight;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.bugemon.dto.BugemonDTO;
import views.utils.SpriteLoader;

import java.io.IOException;

/**
 * Manages the UI elements (sprites, HP, labels) of the currently active Bugémons
 * on the battlefield for both the player and the enemy.
 */
public class FightActiveBugemon {

    @FXML private Label playerNameLabel;
    @FXML private Label playerHpLabel;
    @FXML private Label playerEffectLabel;
    @FXML private ProgressBar playerHpBarFill;
    @FXML private ImageView playerSprite;

    @FXML private Label enemyNameLabel;
    @FXML private Label enemyHpLabel;
    @FXML private Label enemyEffectLabel;
    @FXML private ProgressBar enemyHpBarFill;
    @FXML private ImageView enemySprite;

    private FightChoicePanelSubView choicePanelController;

    public void setChoicePanelController(FightChoicePanelSubView choicePanelController) {
        this.choicePanelController = choicePanelController;
    }

    public ImageView getPlayerSprite() { return playerSprite; }
    public ImageView getEnemySprite() { return enemySprite; }
    public ProgressBar getPlayerHpBarFill() { return playerHpBarFill; }
    public ProgressBar getEnemyHpBarFill() { return enemyHpBarFill; }

    /**
     * Updates the sprite, name, HP, effect label, and (for the player) attack buttons
     * for the given Bugémon.
     *
     * @param bugemon the Bugémon DTO to display
     */
    public void updateActiveBugemonSprite(BugemonDTO bugemon) {
        updateBugemonHPAndEffectDisplay(bugemon);

        Label nameLabel = bugemon.getIsPlayer() ? playerNameLabel : enemyNameLabel;
        Label hpLabel = bugemon.getIsPlayer() ? playerHpLabel : enemyHpLabel;
        Label effectLabel = bugemon.getIsPlayer() ? playerEffectLabel : enemyEffectLabel;
        ImageView sprite = bugemon.getIsPlayer() ? playerSprite : enemySprite;
        
        handleVisibility(bugemon, true);
        sprite.setOpacity(1.0);
        nameLabel.setText(bugemon.getName() + (bugemon.getIsPlayer() ? " (niveau " + bugemon.getlevel() + ")" : ""));
        hpLabel.setText("HP: " + bugemon.getcurrenPv() + "/" + bugemon.getPv());
        
        int atk = bugemon.getAttackModifier();
        int def = bugemon.getDefenseModifier();
        int init = bugemon.getInitiativeModifier();
        effectLabel.setText("Effets :"
                + "\n attaque : " + (atk > 0 ? "+" + atk : atk)
                + "\n defense : " + (def > 0 ? "+" + def : def)
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

        if (bugemon.getIsPlayer()) {
            choicePanelController.updateAttackButtons(bugemon);
        }
        sprite.setVisible(true);
    }

    /**
     * Updates both the HP progress bar and the HP label for the given Bugémon.
     *
     * @param bugemon the Bugémon DTO whose HP to update
     */
    public void updateBugemonHPAndEffectDisplay(BugemonDTO bugemon) {
        ProgressBar hpBar = bugemon.getIsPlayer() ? playerHpBarFill : enemyHpBarFill;
        Label hpLabel = bugemon.getIsPlayer() ? playerHpLabel : enemyHpLabel;
        Label effectLabel = bugemon.getIsPlayer() ? playerEffectLabel : enemyEffectLabel;

        float ratio = Math.round((bugemon.getcurrenPv() / (float) bugemon.getPv()) * 10000f) / 10000f;
        hpBar.setProgress(ratio);
        hpLabel.setText("HP: " + bugemon.getcurrenPv() + "/" + bugemon.getPv());

        int atk = bugemon.getAttackModifier();
        int def = bugemon.getDefenseModifier();
        int init = bugemon.getInitiativeModifier();
        effectLabel.setText("Effets :"
                + "\n attaque : " + (atk > 0 ? "+" + atk : atk)
                + "\n defense : " + (def > 0 ? "+" + def : def)
                + "\n initiative : " + (init > 0 ? "+" + init : init));
    }

    /**
     * Toggles the visibility of all UI components linked to the given Bugémon.
     *
     * @param bugemon       the Bugémon DTO
     * @param mustBeVisible true to show components, false to hide them
     */
    public void handleVisibility(BugemonDTO bugemon, boolean mustBeVisible) {
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

    /**
     * Clears all sprites, HP bars, and labels from the battlefield.
     */
    public void resetDisplay() {
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
    }
}
