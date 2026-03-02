package ulb.view.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ulb.models.Bugemon.TeamBugemon;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class PreBuildTeamController implements Initializable {
    // Liste statique : partagée entre toutes les navigations, perdue à la fermeture
    static final ObservableList<TeamBugemon> teams =
            FXCollections.observableArrayList(new ArrayList<>());

    @FXML private ListView<TeamBugemon> teamListView;
    @FXML private Button btnPlay;
    @FXML private Button btnRename;
    @FXML private Button btnDelete;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teamListView.setItems(teams);

        // Désactiver les boutons si aucune équipe sélectionnée
        btnPlay.disableProperty().bind(
                teamListView.getSelectionModel().selectedItemProperty().isNull());
        btnRename.disableProperty().bind(
                teamListView.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(
                teamListView.getSelectionModel().selectedItemProperty().isNull());
        // Afficher : "Nom Équipe (nombre de Bugémon)"
        teamListView.setCellFactory(lv -> new ListCell<TeamBugemon>() {
            @Override
            protected void updateItem(TeamBugemon item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    int n = item.getSize();
                    setText(item.getName() + " (" + n + " Bugémon" + (n > 1 ? "s" : "") + ")");
                }
            }
        });
    }
    // Retour au menu principal
    @FXML
    private void switchToMenuView(ActionEvent event) throws IOException {
        // Charge la vue du menu principal
        Parent root = FXMLLoader.load(getClass().getResource("menuview.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    // Lancer une partie avec l'équipe sélectionnée
    @FXML
    private void onPlay(ActionEvent event) {
        TeamBugemon selected = teamListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        showInfo("Lancement", "Partie lancée avec « " + selected.getName() + " » !");
    }
    // Renommer l'équipe sélectionnée
    @FXML
    private void onRename(ActionEvent event) {
        TeamBugemon selected = teamListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle("Renommer l'équipe");
        dialog.setHeaderText("Nouveau nom pour « " + selected.getName() + " »");
        dialog.setContentText("Nom :");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newName -> {
            if (!newName.trim().isEmpty()) {
                selected.setName(newName.trim());
                teamListView.refresh();
            }
        }
        );
    }
    // Supprimer l'équipe sélectionnée après confirmation
    @FXML
    private void onDelete(ActionEvent event) {
        TeamBugemon selected = teamListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Supprimer l'équipe");
        confirm.setHeaderText("Supprimer « " + selected.getName() + " » ?");
        confirm.setContentText("Cette action est irréversible.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            teams.remove(selected);
        }
    }
    // Aller à la sélection pour créer une nouvelle équipe
    @FXML
    private void onNewTeam(ActionEvent event) throws IOException {
        navigateTo(event, "selectionview.fxml");
    }
    private void navigateTo(ActionEvent event, String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
