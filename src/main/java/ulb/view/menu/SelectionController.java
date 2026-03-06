package ulb.view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import ulb.models.Bugemon.Bugemon;
import ulb.models.Bugemon.BugemonProvider;
import ulb.models.Bugemon.TeamBugemon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SelectionController implements Initializable {

    private TeamBugemon teamBeingEdited = null;

    @FXML private GridPane gridPane;
    @FXML private VBox teamRectangle;
    @FXML private GridPane teamGrid;

    // Popup flottant avec une Label dedans — aucun impact sur le layout
    private final Popup statsPopup = new Popup();
    private final Label statsLabel = new Label();

    private final List<Bugemon> displayedBugemons = new ArrayList<>();
    private final List<Button> selectedButtons = new ArrayList<>();
    private final TeamBugemon playerTeam = new TeamBugemon();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configuration de la Label dans le Popup
        statsLabel.setMouseTransparent(true);
        statsLabel.setStyle(
                "-fx-background-color: rgba(0,0,0,0.85);" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 6;" +
                        "-fx-background-radius: 5;" +
                        "-fx-font-size: 12;"
        );
        statsPopup.getContent().add(statsLabel);
        // Le popup se ferme si on clique ailleurs
        statsPopup.setAutoHide(true);

        displayBugemons();
    }

    public void switchToMenuView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menuview.fxml"));
        ((Node) event.getSource()).getScene().setRoot(root);
    }

    private void displayBugemons() {
        List<Bugemon> allBugemons = BugemonProvider.getAllBugemons();
        List<Node> buttons = gridPane.getChildren().filtered(node -> node instanceof Button);

        for (int i = 0; i < allBugemons.size() && i < buttons.size(); i++) {
            Bugemon bugemon = allBugemons.get(i);
            Button btn = (Button) buttons.get(i);
            displayedBugemons.add(bugemon);
            btn.setText(bugemon.getNom());

            // Chargement de l'image
            try {
                String path = "/Assets/png/" + bugemon.getSprite();
                Image img = new Image(getClass().getResourceAsStream(path));
                ImageView view = new ImageView(img);
                view.setFitHeight(50);
                view.setPreserveRatio(true);
                btn.setGraphic(view);
                btn.setContentDisplay(ContentDisplay.TOP);
            } catch (Exception e) {
                System.err.println("Impossible de charger l'image pour : " + bugemon.getNom());
            }

            // Affichage des stats au survol
            btn.setOnMouseEntered(event -> {
                String stats = "PV: " + BugemonProvider.getSTATS(bugemon.getId(), "pv") + "\n"
                        + "ATK: " + BugemonProvider.getSTATS(bugemon.getId(), "attaque") + "\n"
                        + "DEF: " + BugemonProvider.getSTATS(bugemon.getId(), "defense") + "\n"
                        + "INIT: " + BugemonProvider.getSTATS(bugemon.getId(), "initiative") + "\n"
                        + "Attaques: " + BugemonProvider.getSTATS(bugemon.getId(), "attaques");

                statsLabel.setText(stats);
                // Coordonnées écran absolues — pas de calcul relatif nécessaire
                statsPopup.show(btn, event.getScreenX() + 15, event.getScreenY() + 15);
            });

            btn.setOnMouseMoved(event -> {
                statsPopup.setX(event.getScreenX() + 15);
                statsPopup.setY(event.getScreenY() + 15);
            });

            btn.setOnMouseExited(event -> statsPopup.hide());

            // Clic = toggle sélection
            final int index = i;
            btn.setOnAction(e -> toggleSelection(btn, displayedBugemons.get(index)));
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

    private void updateTeamGridDisplay() {
        teamGrid.getChildren().clear();
        List<Bugemon> currentTeam = playerTeam.getTeam();

        for (int i = 0; i < currentTeam.size(); i++) {
            Bugemon bugemon = currentTeam.get(i);
            try {
                String path = "/Assets/png/" + bugemon.getSprite();
                Image img = new Image(getClass().getResourceAsStream(path));
                ImageView view = new ImageView(img);
                view.setFitHeight(60);
                view.setFitWidth(60);
                view.setPreserveRatio(true);

                int col = i % 3;
                int row = i / 3;
                teamGrid.add(view, col, row);
            } catch (Exception e) {
                System.err.println("Impossible de charger l'image miniature pour : " + bugemon.getNom());
            }
        }
    }

    @FXML
    public void loadTeamForEditing(TeamBugemon team) {
        this.teamBeingEdited = team;
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

    public void confirmSelection(ActionEvent event) throws IOException {
        if (playerTeam.getSize() < 1) {
            showError("Aucun Bugémon", "Sélectionnez au moins 1 Bugémon avant de valider.");
            return;
        }

        if (teamBeingEdited != null) {
            teamBeingEdited.getTeam().clear();
            teamBeingEdited.getTeam().addAll(playerTeam.getTeam());
        } else {
            TextInputDialog dialog = new TextInputDialog("Mon équipe");
            dialog.setTitle("Sauvegarder l'équipe");
            dialog.setHeaderText("Donnez un nom à votre équipe (" + playerTeam.getSize() + " Bugémon(s))");
            dialog.setContentText("Nom :");
            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent() || result.get().trim().isEmpty()) return;
            playerTeam.setName(result.get().trim());
            PreBuildTeamController.teams.add(playerTeam);
        }
        navigateTo(event, "prebuildteam.fxml");
    }

    private void navigateTo(ActionEvent event, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        ((Node) event.getSource()).getScene().setRoot(root);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}