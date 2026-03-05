package ulb.view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
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

    public void switchToMenuView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menuview.fxml"));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private GridPane gridPane;
    @FXML
    private Pane pane;
    private final Label statsPopup = new Label("testLabel");
    // Bugémons dans le même ordre que les boutons de la grille
    private final List<Bugemon> displayedBugemons = new ArrayList<>();
    // Boutons actuellement sélectionnés (dans l'équipe)
    private final List<Button> selectedButtons = new ArrayList<>();
    private final TeamBugemon playerTeam = new TeamBugemon();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statsPopup.setMouseTransparent(true);
        statsPopup.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 5;");
        statsPopup.setVisible(false);
        pane.getChildren().add(statsPopup);
        pane.setMouseTransparent(true);

        displayBugemons();
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
                ImageView view = new ImageView(img);
                view.setFitHeight(50);
                view.setPreserveRatio(true);
                btn.setGraphic(view);
                btn.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
            }
            catch (Exception e) {
                System.err.println("Impossible de charger l'image pour : " + bugemon.getNom());
            }
            // Affichage des stats au survol
            btn.setOnMouseEntered(event -> {
                String stats = "PV: " + BugemonProvider.getSTATS(bugemon.getId(), "pv") + "\n"
                        + "ATK: " + BugemonProvider.getSTATS(bugemon.getId(), "attaque") + "\n"
                        + "DEF: " + BugemonProvider.getSTATS(bugemon.getId(), "defense") + "\n"
                        + "INIT: " + BugemonProvider.getSTATS(bugemon.getId(), "initiative") + "\n"
                        + "Attaques: " + BugemonProvider.getSTATS(bugemon.getId(), "attaques");

                statsPopup.setText(stats);
                statsPopup.setVisible(true);
                statsPopup.setLayoutX(event.getSceneX() - pane.localToScene(0,0).getX() + 15);
                statsPopup.setLayoutY(event.getSceneY() - pane.localToScene(0,0).getY() + 15);
            });

            btn.setOnMouseMoved(event -> {
                statsPopup.setLayoutX(event.getSceneX() - pane.localToScene(0,0).getX() + 15);
                statsPopup.setLayoutY(event.getSceneY() - pane.localToScene(0,0).getY() + 15);
            });

            btn.setOnMouseExited(event -> {
                statsPopup.setVisible(false);
            });
            // Clic = toggle sélection
            final int index = i;
            btn.setOnAction(e -> toggleSelection(btn, displayedBugemons.get(index)));
        }
    }
    // Selection
    private void toggleSelection(Button btn, Bugemon bugemon) {
        if (selectedButtons.contains(btn)) {
            // Déselectionner
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
    }
    // Validation & sauvegarde

    @FXML
    public void confirmSelection(ActionEvent event) throws IOException {
        if (playerTeam.getSize() < 1) {
            showError("Aucun Bugémon", "Sélectionnez au moins 1 Bugémon avant de valider.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("Mon équipe");
        dialog.setTitle("Sauvegarder l'équipe");
        dialog.setHeaderText("Donnez un nom à votre équipe (" + playerTeam.getSize() + " Bugémon(s))");
        dialog.setContentText("Nom :");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent() || result.get().trim().isEmpty()) return;

        playerTeam.setName(result.get().trim());

        // Ajouter directement à la liste en mémoire
        PreBuildTeamController.teams.add(playerTeam);

        // Retour à la page des équipes
        navigateTo(event, "prebuildteam.fxml");
    }
    private void navigateTo(ActionEvent event, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
