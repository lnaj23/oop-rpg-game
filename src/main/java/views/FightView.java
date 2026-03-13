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

import models.Bugemon.DTO.BugemonDTO;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

import javafx.stage.Popup;
import javafx.scene.input.MouseEvent;


public class FightView extends View<FightController> {

    private final Popup HPPopup = new Popup();
    private final Label HPLabel = new Label();

    private final Popup statsPopup = new Popup();
    private final Label statsLabel = new Label();

    // Adversaire
    @FXML private Label     enemyNameLabel;
    @FXML private Label     enemyHpLabel;
    @FXML private Label     enemyEffectLabel;
    @FXML private ProgressBar enemyHpBarFill;

    // Joueur
    @FXML private Label     playerNameLabel;
    @FXML private Label     playerHpLabel;
    @FXML private Label     playerEffectLabel;
    @FXML private ProgressBar playerHpBarFill;

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
        initPopup(HPPopup,HPLabel);
        initPopup(statsPopup,statsLabel);

        btnAttack.setOnAction(e -> showAttackandActionPanels(true)); //afficher attackPanel
        btnItem.setOnAction(e -> this.controller.onItemClicked());
        btnSwitch.setOnAction(e -> this.controller.onSwitchClicked());
        btnForfeit.setOnAction(e -> this.controller.onForfeitClicked());

        btnAttack1.setOnAction(e -> this.controller.onAttackSelected(0));
        btnAttack2.setOnAction(e -> this.controller.onAttackSelected(1));
        btnAttack3.setOnAction(e -> this.controller.onAttackSelected(2));
        btnBackFromAttack.setOnAction(e -> showAttackandActionPanels(false)); //Afficher ActionPanel

    }
    private void initPopup(Popup popup,Label label ) {
        label.setMouseTransparent(true);
        label.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 5;");
        popup.getContent().add(label);
        popup.setAutoHide(true);
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
            imgView.setOnMouseEntered(e -> showHpPopup(e, bugemon, imgView));
            imgView.setOnMouseMoved(this::updatePopupPosition);
            imgView.setOnMouseExited(e -> HPPopup.hide());


            imgView.setOnMouseClicked(e -> {
                if (statsPopup.isShowing()) {
                    statsPopup.hide();
                    showHpPopup(e, bugemon, imgView); // remet le HP popup
                } else {
                    HPPopup.hide();
                    showStatsPopup(e.getScreenX(), e.getScreenY(), bugemon, imgView);
                }
            });

            imgView.setUserData(bugemon);
            teamGrid.add(imgView, i % 3, i / 3);
        }
    }
    private void showStatsPopup(double x, double y, BugemonDTO bugemon, ImageView node) {
        String text = "PV: " + bugemon.getPv() + "\n" +
                "ATK: " + bugemon.getAttaque() + "\n" +
                "DEF: " + bugemon.getDefense() + "\n" +
                "INIT: " + bugemon.getInitiative() + "\n" +
                "Attaques: " + bugemon.getAttackNamesAsString();
        statsLabel.setText(text);
        statsPopup.show(node, x + 15, y + 15);
    }

    private void showHpPopup(MouseEvent event, BugemonDTO bugemon, ImageView node) {

        String text = "HP : " + String.valueOf(bugemon.getPv()) + " / " + String.valueOf(bugemon.getPv());
        //remplacer getPv par les current pv qd ça sera là
        HPLabel.setText(text);

        HPPopup.show(node, event.getScreenX() + 15, event.getScreenY() + 15);
    }

    private void updatePopupPosition(MouseEvent event) {
        HPPopup.setX(event.getScreenX() + 15);
        HPPopup.setY(event.getScreenY() + 15);
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

    public void updateActiveBugemonSprite(BugemonDTO bugemon, boolean isPlayer) {

        Label nameLabel   = isPlayer ? playerNameLabel   : enemyNameLabel;
        Label hpLabel     = isPlayer ? playerHpLabel     : enemyHpLabel;
        Label effectLabel = isPlayer ? playerEffectLabel : enemyEffectLabel;
        ImageView sprite  = isPlayer ? playerSprite      : enemySprite;

        nameLabel.setText(bugemon.getNom() + (isPlayer ? " (Player)" : ""));
        hpLabel.setText("HP: " + bugemon.getPv() + "/" + bugemon.getPv()); //remplacer getPv par les current pv qd ça sera là
        effectLabel.setText("Effets : Aucun");

        String path = "/Assets/png/" + bugemon.getSprite();
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        sprite.setImage(img);

        if (isPlayer) {
            updateAttackButtonName(bugemon);
        }
    }

    public void markPlayerBugemonAsFainted(BugemonDTO bugemon) {
        for (Node node : teamGrid.getChildren()) {
            if (node instanceof ImageView imgView && Objects.equals(bugemon.getId(), ((BugemonDTO) imgView.getUserData()).getId())) {
                imgView.setEffect(new ColorAdjust(0, -1, 0, 0));
                break;
            }
        }
    }

    public void updateAttackButtonName(BugemonDTO bugemon) {
        List<String> attackname = formatAttackNames(bugemon.getAttackNamesAsList());
        List<Button> btns = List.of(btnAttack1, btnAttack2, btnAttack3);

        for (int i = 0; i < btns.size(); i++) {
            if (i < attackname.size()) {
                btns.get(i).setText(attackname.get(i));
                btns.get(i).setVisible(true);
            } else {
                btns.get(i).setVisible(false);
            }
        }
    }

    private List<String> formatAttackNames(List<String> raw) {
        return raw.stream()
                .map(s -> Arrays.stream(s.split("_"))
                        .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                        .collect(Collectors.joining(" ")))
                .toList();
    }

    public void updateBugemonHP(BugemonDTO bugemon, boolean isPlayer, float currentHP){
        updateHPBarFill(bugemon, isPlayer, currentHP);
        updatehpLabel(bugemon, isPlayer, currentHP);
    }

    public void updateHPBarFill(BugemonDTO bugemon, boolean isPlayer, float currentHP) {

        ProgressBar hpbar = isPlayer ? playerHpBarFill : enemyHpBarFill;

        float maxHP = bugemon.getPv();
        float ratio = Math.round((currentHP / maxHP) * 10000f) / 10000f;

        hpbar.setProgress(ratio);
    }

    private void updatehpLabel(BugemonDTO bugemon, boolean isPlayer, float currentHP) {

        Label hpLabel     = isPlayer ? playerHpLabel     : enemyHpLabel;

        hpLabel.setText("HP: " + currentHP + "/" + bugemon.getPv());

    }

    public void toggleAllButtonDisability() {
        boolean disabled = !btnItem.isDisabled();
        for (Node node : actionPanel.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(disabled);
            }
        }
    }


    @Override
    protected String getTitle() {
        return "Bugémon – Combat";
    }
}
