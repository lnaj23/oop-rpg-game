package views;

import controllers.FightController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class FightView extends View<FightController> {

    // Adversaire
    @FXML private Label     enemyNameLabel;
    @FXML private Label     enemyHpLabel;
    @FXML private Rectangle enemyHpBarFill;

    // Joueur
    @FXML private Label     playerNameLabel;
    @FXML private Label     playerHpLabel;
    @FXML private Rectangle playerHpBarFill;

    // Message durant le combat
    @FXML private Label combatMessageLabel;

    // le pannel des 4 boutons
    @FXML private GridPane actionPanel;
    @FXML private Button   btnAttack;
    @FXML private Button   btnItem;
    @FXML private Button   btnSwitch;
    @FXML private Button   btnForfeit;

    // le pannel d'attaque
    @FXML private GridPane attackPanel;
    @FXML private Button   btnAttack1;
    @FXML private Button   btnAttack2;
    @FXML private Button   btnAttack3;
    @FXML private Button   btnBackFromAttack;

    // les sprites
    @FXML private ImageView playerSprite;
    @FXML private ImageView enemySprite;

    public FightView() throws IOException {
        super("/fxml/fightview.fxml");

        // le panel principal
        btnAttack.setOnAction(e -> showAttackPanel());
        btnItem.setOnAction(e -> this.controller.onItemClicked());
        btnSwitch.setOnAction(e -> this.controller.onSwitchClicked());
        btnForfeit.setOnAction(e -> this.controller.onForfeitClicked());

        // le panel attaques
        btnAttack1.setOnAction(e -> this.controller.onAttackSelected(0));
        btnAttack2.setOnAction(e -> this.controller.onAttackSelected(1));
        btnAttack3.setOnAction(e -> this.controller.onAttackSelected(2));
        btnBackFromAttack.setOnAction(e -> showActionPanel());
    }

    public void showAttackPanel() {
        actionPanel.setVisible(false);
        actionPanel.setManaged(false);
        attackPanel.setVisible(true);
        attackPanel.setManaged(true);
    }

    public void showActionPanel() {
        attackPanel.setVisible(false);
        attackPanel.setManaged(false);
        actionPanel.setVisible(true);
        actionPanel.setManaged(true);
    }

    public void setCombatMessage(String message) {
        combatMessageLabel.setText(message);
    }

    public void setAttackButtonNames(String name1, String name2, String name3) {
        btnAttack1.setText(name1);
        btnAttack2.setText(name2);
        btnAttack3.setText(name3);
    }

    public void setPlayerSprite(String spritePath) {
        Image img = new Image(getClass().getResourceAsStream("/Assets/png/" + spritePath));
        playerSprite.setImage(img);
    }

    public void setEnemySprite(String spritePath) {
        Image img = new Image(getClass().getResourceAsStream("/Assets/png/" + spritePath));
        enemySprite.setImage(img);
    }

    @Override
    protected String getTitle() {
        return "Bugémon – Combat";
    }
}
