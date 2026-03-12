package views;

import controllers.FightController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import java.util.List;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.StackPane;
import models.Bugemon.DTO.BugemonDTO;
import java.io.IOException;
import javafx.stage.Popup;
import javafx.scene.input.MouseEvent;


public class FightView extends View<FightController> {

    private final Popup statsPopup = new Popup();
    private final Label statsLabel = new Label();
    // Adversaire
    @FXML private Label     enemyNameLabel;
    @FXML private Label     enemyHpLabel;
    @FXML private Rectangle enemyHpBarFill;

    // Joueur
    @FXML private Label     playerNameLabel;
    @FXML private Label     playerHpLabel;
    @FXML private Label playerEffectLabel;
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

    // tableau 2x3
    @FXML private GridPane teamGrid;
    public FightView() throws IOException {
        super("/fxml/fightview.fxml");
        statsLabel.setMouseTransparent(true);
        statsLabel.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 5;");
        statsPopup.getContent().add(statsLabel);
        statsPopup.setAutoHide(true);
        // le panel principal
        btnAttack.setOnAction(e -> showAttackandActionPanels(true)); //afficher attackPanel
        btnItem.setOnAction(e -> this.controller.onItemClicked());
        btnSwitch.setOnAction(e -> this.controller.onSwitchClicked());
        btnForfeit.setOnAction(e -> this.controller.onForfeitClicked());

        // le panel attaques
        btnAttack1.setOnAction(e -> this.controller.onAttackSelected(0));
        btnAttack2.setOnAction(e -> this.controller.onAttackSelected(1));
        btnAttack3.setOnAction(e -> this.controller.onAttackSelected(2));
        btnBackFromAttack.setOnAction(e -> showAttackandActionPanels(false)); //Afficher ActionPanel

    }

    public void displayTeam(List<BugemonDTO> team) {

        teamGrid.getChildren().clear();

        for (int i = 0; i < team.size(); i++) {

            BugemonDTO bugemon = team.get(i);

            String path = "/Assets/png/" + bugemon.getSprite();
            Image img = new Image(getClass().getResourceAsStream(path));

            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(60);
            imgView.setFitWidth(60);
            imgView.setPreserveRatio(true);

            // SURVOL SOURIS
            imgView.setOnMouseEntered(e -> showStats(e, bugemon, imgView));
            imgView.setOnMouseMoved(this::updatePopupPosition);
            imgView.setOnMouseExited(e -> statsPopup.hide());

            teamGrid.add(imgView, i % 3, i / 3);
        }
    }
    private void showStats(MouseEvent event, BugemonDTO bugemon, ImageView node) {

        String text =
                "PV: " + bugemon.getPv() + "\n" +
                        "ATK: " + bugemon.getAttaque() + "\n" +
                        "DEF: " + bugemon.getDefense() + "\n" +
                        "INIT: " + bugemon.getInitiative() + "\n" +
                        "Attaques: " + bugemon.getAttackNamesAsString();

        statsLabel.setText(text);

        statsPopup.show(node, event.getScreenX() + 15, event.getScreenY() + 15);
    }

    private void updatePopupPosition(MouseEvent event) {
        statsPopup.setX(event.getScreenX() + 15);
        statsPopup.setY(event.getScreenY() + 15);
    }
    public void showAttackandActionPanels(boolean showAttack) {
        attackPanel.setVisible(showAttack);
        attackPanel.setManaged(showAttack);

        actionPanel.setVisible(!showAttack);
        actionPanel.setManaged(!showAttack);
    }

    public void setCombatMessage(String message) {
        combatMessageLabel.setText(message);
    }

    public void setAttackButtonNames(String name1, String name2, String name3) {
        btnAttack1.setText(name1);
        btnAttack2.setText(name2);
        btnAttack3.setText(name3);
    }
    @FXML private ImageView playerSprite1;
    @FXML private ImageView playerSprite2;
    @FXML private ImageView playerSprite3;

    @FXML private ImageView enemySprite1;
    @FXML private ImageView enemySprite2;
    @FXML private ImageView enemySprite3;

    public void setPlayerTeamSprites(List<String> sprites) {
        for (int i = 0; i < sprites.size(); i++) {
            Image img = new Image(getClass().getResourceAsStream("/Assets/png/" + sprites.get(i)));
            switch(i) {
                case 0 -> playerSprite1.setImage(img);
                case 1 -> playerSprite2.setImage(img);
                case 2 -> playerSprite3.setImage(img);
            }
        }
    }

    public void setEnemyTeamSprites(List<String> sprites) {
        for (int i = 0; i < sprites.size(); i++) {
            Image img = new Image(getClass().getResourceAsStream("/Assets/png/" + sprites.get(i)));
            switch(i) {
                case 0 -> enemySprite1.setImage(img);
                case 1 -> enemySprite2.setImage(img);
                case 2 -> enemySprite3.setImage(img);
            }
        }
    }
    public void displayActiveBugemon(List<BugemonDTO> team) {

        if (team == null || team.isEmpty()) {
            return;
        }

        BugemonDTO bugemon = team.get(0);

        // Nom
        playerNameLabel.setText(bugemon.getNom());

        // HP
        playerHpLabel.setText("HP: " + bugemon.getPv() + "/" + bugemon.getPv());

        // Effet
        playerEffectLabel.setText("Effets : Aucun");

        // Barre de vie
        playerHpBarFill.setWidth(200);

        // Image
        String path = "/Assets/png/" + bugemon.getSprite();
        Image img = new Image(getClass().getResourceAsStream(path));

        playerSprite.setImage(img);
    }
    @Override
    protected String getTitle() {
        return "Bugémon – Combat";
    }
}
