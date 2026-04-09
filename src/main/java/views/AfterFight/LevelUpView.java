package views.AfterFight;

import configuration.AppConfig;
import controllers.AfterFight.LevelUpController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.bugemon.dto.BugemonDTO;
import views.View;


import java.io.IOException;
import java.util.Objects;

public class LevelUpView extends View<LevelUpController> {

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
    public void updateBugemonSprite (BugemonDTO bugemon) {
        Image img = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(AppConfig.PNG_PREFIX + bugemon.getSprite())));
        bugemonImage.setImage(img);
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
     * Updates the text displayed on the three option buttons.
     *
     * @param option1 the text for the first option
     * @param option2 the text for the second option
     * @param option3 the text for the third option
     */
    public void updateOptionTexts(String option1, String option2, String option3) {
        btnOption1.setText(option1);
        btnOption2.setText(option2);
        btnOption3.setText(option3);
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
