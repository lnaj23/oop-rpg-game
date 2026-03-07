package views;

import controllers.SelectionController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import models.Bugemon.Bugemon;
import models.Bugemon.BugemonProvider;
import models.Bugemon.TeamBugemon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SelectionView extends View {

    private SelectionController controller;

    @FXML private GridPane gridPane;
    @FXML private GridPane teamGrid;
    @FXML private VBox teamRectangle;
    @FXML private Label bugemonStatsLabel;
    @FXML private Button confirmButton;
    @FXML private Button backButton;

    private final Popup statsPopup = new Popup();
    private final Label statsLabel = new Label();
    private final List<Bugemon> displayedBugemons = new ArrayList<>();
    private final List<Button> selectedButtons = new ArrayList<>();
    private TeamBugemon playerTeam = new TeamBugemon();

    public SelectionView() throws IOException {
        super("/fxml/selectionview.fxml");

        // Setup popup de stats au survol
        statsLabel.setMouseTransparent(true);
        statsLabel.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 5;");
        statsPopup.getContent().add(statsLabel);
        statsPopup.setAutoHide(true);

        backButton.setOnAction(e -> this.controller.goToMenu());
        confirmButton.setOnAction(e -> onConfirmClicked());

        displayBugemons();
    }

    public void setController(SelectionController controller) {
        this.controller = controller;
    }

    private void displayBugemons() {
        List<Bugemon> allBugemons = BugemonProvider.getAllBugemons();
        List<Node> buttons = gridPane.getChildren().filtered(node -> node instanceof Button);

        for (int i = 0; i < allBugemons.size() && i < buttons.size(); i++) {
            Bugemon bugemon = allBugemons.get(i);
            Button btn = (Button) buttons.get(i);
            displayedBugemons.add(bugemon);
            btn.setText(bugemon.getNom());

            try {
                String path = "/Assets/png/" + bugemon.getSprite();
                Image img = new Image(getClass().getResourceAsStream(path));
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(50);
                imgView.setPreserveRatio(true);
                btn.setGraphic(imgView);
                btn.setContentDisplay(ContentDisplay.TOP);
            } catch (Exception e) {
                System.err.println("Impossible de charger l'image pour : " + bugemon.getNom());
            }

            // Popup de stats au survol
            btn.setOnMouseEntered(event -> {
                String stats = "PV: " + BugemonProvider.getSTATS(bugemon.getId(), "pv") + "\n"
                        + "ATK: " + BugemonProvider.getSTATS(bugemon.getId(), "attaque") + "\n"
                        + "DEF: " + BugemonProvider.getSTATS(bugemon.getId(), "defense") + "\n"
                        + "INIT: " + BugemonProvider.getSTATS(bugemon.getId(), "initiative") + "\n"
                        + "Attaques: " + BugemonProvider.getSTATS(bugemon.getId(), "attaques");
                statsLabel.setText(stats);
                statsPopup.show(btn, event.getScreenX() + 15, event.getScreenY() + 15);
            });
            btn.setOnMouseMoved(event -> {
                statsPopup.setX(event.getScreenX() + 15);
                statsPopup.setY(event.getScreenY() + 15);
            });
            btn.setOnMouseExited(event -> statsPopup.hide());

            // Clic = toggle sélection
            final int index = i;
            btn.setOnAction(e -> {
                Bugemon selected = displayedBugemons.get(index);
                toggleSelection(btn, selected);
                showBugemonStats(selected);
            });
        }
    }

    private void toggleSelection(Button btn, Bugemon bugemon) {
        if (selectedButtons.contains(btn)) {
            selectedButtons.remove(btn);
            playerTeam.getTeam().remove(bugemon);
            btn.setStyle("");
        } else {
            if (selectedButtons.size() >= 6) {
                showError("Équipe pleine", "Vous ne pouvez sélectionner que 6 Bugémons maximum.");
                return;
            }
            selectedButtons.add(btn);
            playerTeam.addBugemon(bugemon);
            btn.setStyle("-fx-background-color: #a8d8a8; -fx-border-color: #4caf50; -fx-border-width: 2;");
        }
        updateTeamGridDisplay();
    }

    private void showBugemonStats(Bugemon bugemon) {
        bugemonStatsLabel.setText(String.format(
                "%s | PV: %s | ATK: %s | DEF: %s | INIT: %s",
                bugemon.getNom(),
                BugemonProvider.getSTATS(bugemon.getId(), "pv"),
                BugemonProvider.getSTATS(bugemon.getId(), "attaque"),
                BugemonProvider.getSTATS(bugemon.getId(), "defense"),
                BugemonProvider.getSTATS(bugemon.getId(), "initiative")
        ));
    }

    private void updateTeamGridDisplay() {
        teamGrid.getChildren().clear();
        List<Bugemon> currentTeam = playerTeam.getTeam();
        for (int i = 0; i < currentTeam.size(); i++) {
            Bugemon bugemon = currentTeam.get(i);
            try {
                String path = "/Assets/png/" + bugemon.getSprite();
                Image img = new Image(getClass().getResourceAsStream(path));
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(60);
                imgView.setFitWidth(60);
                imgView.setPreserveRatio(true);
                teamGrid.add(imgView, i % 3, i / 3);
            } catch (Exception e) {
                System.err.println("Impossible de charger l'image miniature pour : " + bugemon.getNom());
            }
        }
    }

    // Appelée par SelectionController.loadTeamForEditing()
    public void preSelectTeam(TeamBugemon team) {
        List<Node> buttons = gridPane.getChildren().filtered(n -> n instanceof Button);
        for (int i = 0; i < displayedBugemons.size() && i < buttons.size(); i++) {
            Bugemon currentBug = displayedBugemons.get(i);
            if (team.getTeam().stream().anyMatch(b -> b.getId().equals(currentBug.getId()))) {
                Button btn = (Button) buttons.get(i);
                selectedButtons.add(btn);
                playerTeam.addBugemon(currentBug);
                btn.setStyle("-fx-background-color: #a8d8a8; -fx-border-color: #4caf50; -fx-border-width: 2;");
            }
        }
        updateTeamGridDisplay();
    }

    // Réinitialise toute la sélection
    public void reset() {
        for (Button btn : selectedButtons) btn.setStyle("");
        selectedButtons.clear();
        playerTeam = new TeamBugemon();
        teamGrid.getChildren().clear();
        bugemonStatsLabel.setText("Clique sur un Bugémon pour voir ses statistiques");
    }

    private void onConfirmClicked() {
        if (playerTeam.getSize() < 1) {
            showError("Aucun Bugémon", "Sélectionnez au moins 1 Bugémon avant de valider.");
            return;
        }
        if (this.controller.isEditing()) {
            this.controller.confirmSelection(playerTeam, null);
        } else {
            TextInputDialog dialog = new TextInputDialog("Mon équipe");
            dialog.setTitle("Sauvegarder l'équipe");
            dialog.setHeaderText("Donnez un nom à votre équipe (" + playerTeam.getSize() + " Bugémon(s))");
            dialog.setContentText("Nom :");
            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent() || result.get().trim().isEmpty()) return;
            this.controller.confirmSelection(playerTeam, result.get().trim());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    protected String getTitle() {
        return "Sélection d'équipe - Bugémon";
    }
}