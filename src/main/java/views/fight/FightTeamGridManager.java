package views.fight;

import configuration.AppConfig;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import models.bugemon.dto.BugemonDTO;
import views.utils.PopupManager;
import views.utils.SpriteLoader;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class FightTeamGridManager {

    private final GridPane teamGrid;
    private final GridPane enemyGrid;
    private final PopupManager popup;
    private final Popup hpPopup;

    public FightTeamGridManager(GridPane teamGrid, GridPane enemyGrid,
                                PopupManager popup, Popup hpPopup) {
        this.teamGrid = teamGrid;
        this.enemyGrid = enemyGrid;
        this.popup = popup;
        this.hpPopup = hpPopup;
    }

    public void displayTeam(List<BugemonDTO> team) throws IOException {
        if (team == null || team.isEmpty()) {
            return;
        }

        boolean isPlayerTeam = team.get(0).isPlayer();
        GridPane grid = isPlayerTeam ? teamGrid : enemyGrid;

        grid.getChildren().clear();

        for (int i = 0; i < team.size(); i++) {
            BugemonDTO bugemon = team.get(i);

            ImageView sprite = isPlayerTeam
                    ? createBugemonSprite(bugemon)
                    : createNoBugemonSprite();

            sprite.setFitWidth(60);
            sprite.setFitHeight(60);
            sprite.setPreserveRatio(true);

            if (isPlayerTeam) {
                sprite.setUserData(bugemon);
                bindPlayerSpriteEvents(sprite, bugemon);

                if (bugemon.isDefault()) {
                    sprite.setScaleX(-1);
                }
            }

            if (bugemon.isKO()) {
                applyFaintedStyle(sprite);
            }

            grid.add(sprite, i % 3, i / 3);
        }
    }

    public void markBugemonAsFainted(BugemonDTO bugemon) {
        if (bugemon == null) {
            return;
        }

        if (bugemon.isPlayer()) {
            markPlayerBugemonAsFainted(bugemon);
        } else {
            markNextEnemyBugemonAsFainted();
        }
    }

    public void clear() {
        teamGrid.getChildren().clear();
        enemyGrid.getChildren().clear();
    }

    private ImageView createBugemonSprite(BugemonDTO bugemon) throws IOException {
        Image image = SpriteLoader.loadSprite(bugemon);
        return new ImageView(image);
    }

    private ImageView createNoBugemonSprite() {
        Image image = new Image(Objects.requireNonNull(
                FightTeamGridManager.class.getResourceAsStream(
                        AppConfig.PNG_PREFIX + "nobugemon/ulb.png"
                )
        ));

        return new ImageView(image);
    }

    private void bindPlayerSpriteEvents(ImageView sprite, BugemonDTO bugemon) {
        sprite.setOnMouseEntered(e -> popup.showHpPopup(e, bugemon, sprite));
        sprite.setOnMouseMoved(popup::updateHpPosition);
        sprite.setOnMouseExited(e -> hpPopup.hide());

        sprite.setOnMouseClicked(e -> {
            if (popup.isStatsShowing()) {
                popup.hideStats();
                popup.showHpPopup(e, bugemon, sprite);
            } else {
                hpPopup.hide();
                popup.showStatsPopup(e.getScreenX(), e.getScreenY(), bugemon, sprite);
            }
        });
    }

    private void markPlayerBugemonAsFainted(BugemonDTO bugemon) {
        for (Node node : teamGrid.getChildren()) {
            if (node instanceof ImageView sprite && isSameBugemon(sprite.getUserData(), bugemon)) {
                applyFaintedStyle(sprite);
                return;
            }
        }
    }

    private void markNextEnemyBugemonAsFainted() {
        for (Node node : enemyGrid.getChildren()) {
            if (node instanceof ImageView sprite && !isAlreadyFainted(sprite)) {
                applyFaintedStyle(sprite);
                return;
            }
        }
    }

    private boolean isSameBugemon(Object data, BugemonDTO bugemon) {
        if (!(data instanceof BugemonDTO displayedBugemon)) {
            return false;
        }

        if (displayedBugemon == bugemon) {
            return true;
        }

        if (bugemon.getId() != null
                && displayedBugemon.getId() != null
                && bugemon.getId().equals(displayedBugemon.getId())) {
            return true;
        }

        return Objects.equals(bugemon.getName(), displayedBugemon.getName())
                && Objects.equals(bugemon.getSprite(), displayedBugemon.getSprite())
                && bugemon.isDefault() == displayedBugemon.isDefault();
    }

    private void applyFaintedStyle(ImageView sprite) {
        ColorAdjust grayFilter = new ColorAdjust();

        grayFilter.setSaturation(-1);
        grayFilter.setBrightness(-0.35);

        sprite.setEffect(grayFilter);
        sprite.setOpacity(0.45);
    }

    private boolean isAlreadyFainted(ImageView sprite) {
        return sprite.getEffect() instanceof ColorAdjust;
    }
}