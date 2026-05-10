package views.afterfight;

import configuration.AppConfig;
import controllers.afterfight.LevelUpController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import models.bugemon.dto.BugemonDTO;
import models.game.LevelUpBonus;
import services.view.formatter.LevelUpBonusFormatter;
import views.View;
import services.view.assets.SpriteLoader;


import java.io.IOException;
import java.util.List;

public class LevelUpView extends View {

    private LevelUpController controller;

    /**
     * Sets the controller used by this view to delegate user actions.
     *
     * @param controller the controller associated with this view
     */
    public void setController(LevelUpController controller) {
        this.controller = controller;
    }

    @FXML private Button btnOption1;
    @FXML private Button btnOption2;
    @FXML private Button btnOption3;

    @FXML private ImageView bugemonImage;
    @FXML private Label levelUpLabel;
    /**
     * Creates the level up view by loading its FXML file.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public LevelUpView() throws IOException {
        super(AppConfig.FXML_LEVELUP);
    }

    /**
     * Initializes the view by binding each button
     * to the corresponding controller action.
     */
    @FXML
    public void initialize() {
        btnOption1.setOnAction(e -> controller.onOptionChosen(1));
        btnOption2.setOnAction(e -> controller.onOptionChosen(2));
        btnOption3.setOnAction(e -> controller.onOptionChosen(3));
    }

    /**
     * Updates the displayed sprite of the Bugemon that leveled up.
     *
     * @param bugemon the Bugemon whose sprite must be displayed
     */
    public void updateBugemonSprite (BugemonDTO bugemon) throws IOException {
        bugemonImage.setImage(SpriteLoader.loadSprite(bugemon));
    }

    /**
     * Updates the level up message with the Bugemon name and its new level.
     *
     * @param bugemon the Bugemon that leveled up
     */
    public void updateLevelUpText(BugemonDTO bugemon, int displayedLevel) {
        levelUpLabel.setText(bugemon.getName() + " a atteint le niveau " + displayedLevel + "!");
    }

    /**
     * Updates the text displayed on the three option buttons by formatting
     * the given level-up bonuses internally.
     */
    public void updateOptions(List<LevelUpBonus> options) {
        btnOption1.setText(LevelUpBonusFormatter.format(options.get(0)));
        btnOption2.setText(LevelUpBonusFormatter.format(options.get(1)));
        btnOption3.setText(LevelUpBonusFormatter.format(options.get(2)));
    }

    /**
     * Returns the title of the level up window.
     *
     * @return the window title
     */
    @Override
    protected String getTitle() {
        return "Level Up";
    }
}
