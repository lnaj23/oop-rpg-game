package views.fight;

import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.fxml.FXML;
import models.bugemon.dto.BugemonDTO;
import views.utils.PopupManager;
import views.utils.SpriteLoader;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Manages the player and enemy team grids in the fight view.
 * <p>
 * Extracted from {@link FightView} to respect the Single Responsibility Principle.
 * Responsible for: displaying team sprites in the grids, binding hover/click
 * popup events on player sprites, and marking/unmarking fainted Bugémon
 * with a greyscale effect.
 * </p>
 */
public class FightTeamGridManager {

    @FXML private GridPane teamGrid;
    @FXML private GridPane enemyGrid;

    private PopupManager popup;
    private Popup hpPopup;

    public void setPopups(PopupManager popup, Popup hpPopup) {
        this.popup = popup;
        this.hpPopup = hpPopup;
    }

    /**
     * Populates the player's or enemy's team grid with sprites for each Bugémon.
     * Player sprites show an HP popup on hover and toggle a stats popup on click.
     *
     * @param team the list of Bugémon DTOs to display
     */
    public void displayTeam(List<BugemonDTO> team) throws IOException {
        boolean  isPlayer = team.getFirst().isPlayer();
        GridPane grid     = isPlayer ? teamGrid : enemyGrid;
        grid.getChildren().clear();

        for (int i = 0; i < team.size(); i++) {
            BugemonDTO bugemon = team.get(i);
            ImageView imgView = createSprite(60, bugemon);
            imgView.setUserData(bugemon);

            if (isPlayer) {
                bindTeamSpriteEvents(imgView, bugemon);
                if (bugemon.isDefault()) {
                    imgView.setScaleX(-1);
                }
            }

            if (bugemon.isKO()) {
                imgView.setEffect(new ColorAdjust(0, -1, 0, 0));
            }

            grid.add(imgView, i % 3, i / 3);
        }
    }

    /**
     * Applies a greyscale effect to the team grid sprite of the given fainted Bugémon.
     *
     * @param bugemon the fainted Bugémon DTO
     */
    public void markBugemonAsFainted(BugemonDTO bugemon) {
        GridPane grid = bugemon.isPlayer() ? teamGrid : enemyGrid;
        for (Node node : grid.getChildren()) {
            if (node instanceof ImageView iv && isSpriteOfBugemon(iv, bugemon)) {
                iv.setEffect(new ColorAdjust(0, -1, 0, 0));
                break;
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
        GridPane grid = bugemon.isPlayer() ? teamGrid : enemyGrid;
        for (Node node : grid.getChildren()) {
            if (node instanceof ImageView iv && isSpriteOfBugemon(iv, bugemon)) {
                iv.setEffect(null);
                break;
            }
        }
    }

    /**
     * Clears both team grids. Called during display reset.
     */
    public void clear() {
        teamGrid.getChildren().clear();
        enemyGrid.getChildren().clear();
    }

    /**
     * Binds mouse events (enter, move, exit, click) to a team sprite ImageView.
     * Shows HP popup on hover and toggles stats popup on click.
     *
     * @param imgView the ImageView to bind events to
     * @param bugemon the Bugémon DTO associated with this sprite
     */
    private void bindTeamSpriteEvents(ImageView imgView, BugemonDTO bugemon) {
        imgView.setOnMouseEntered(e -> popup.showHpPopup(e, bugemon, imgView));
        imgView.setOnMouseMoved(popup::updateHpPosition);
        imgView.setOnMouseExited(e -> hpPopup.hide());
        imgView.setOnMouseClicked(e -> {
            if (popup.isStatsShowing()) {
                popup.hideStats();
                popup.showHpPopup(e, bugemon, imgView);
            } else {
                hpPopup.hide();
                popup.showStatsPopup(e.getScreenX(), e.getScreenY(), bugemon, imgView);
            }
        });
    }

    /**
     * Creates an ImageView sprite from a resource path with the specified size.
     * Preserves the aspect ratio of the image.
     *
     * @param size    the width and height to fit the image to
     * @param bugemon the Bugémon whose sprite to load
     * @return a configured ImageView with the loaded sprite
     */
    private ImageView createSprite(int size, BugemonDTO bugemon) throws IOException {
        Image     img  = SpriteLoader.loadSprite(bugemon);
        ImageView view = new ImageView(img);
        view.setFitHeight(size);
        view.setFitWidth(size);
        view.setPreserveRatio(true);
        return view;
    }

    /**
     * Checks whether an ImageView is associated with the given Bugémon.
     *
     * @param imageView the sprite node to check
     * @param bugemon   the Bugémon DTO to compare with
     * @return true if the sprite represents the given Bugémon
     */
    private boolean isSpriteOfBugemon(ImageView imageView, BugemonDTO bugemon) {
        Object data = imageView.getUserData();
        return data instanceof BugemonDTO displayedBugemon
                && Objects.equals(bugemon.getId(), displayedBugemon.getId());
    }
}
