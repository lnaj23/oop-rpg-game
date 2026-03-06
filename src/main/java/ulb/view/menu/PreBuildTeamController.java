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
import ulb.models.Bugemon.Bugemon;
import ulb.models.Bugemon.TeamBugemon;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PreBuildTeamController implements Initializable {
    static final ObservableList<TeamBugemon> teams = FXCollections.observableArrayList(new ArrayList<>());

    @FXML private ListView<TeamBugemon> teamListView;
    @FXML private ListView<String> bugemonListView;
    @FXML private Button btnPlay, btnRename, btnDelete, btnModify;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        teamListView.setItems(teams);
        btnPlay.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());
        btnRename.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());
        btnModify.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());

        teamListView.setCellFactory(lv -> new ListCell<TeamBugemon>() {
            @Override
            protected void updateItem(TeamBugemon item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getName() + " (" + item.getSize() + " Bugémon(s))");
            }
        });

        teamListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            bugemonListView.getItems().clear();
            if (newV != null) {
                for (Bugemon b : newV.getTeam()) bugemonListView.getItems().add(b.getNom());
            }
        });
    }

    @FXML
    private void switchToMenuView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menuview.fxml"));
        ((Node) event.getSource()).getScene().setRoot(root);
    }

    @FXML
    private void onModify(ActionEvent event) throws IOException {
        TeamBugemon selected = teamListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("selectionview.fxml"));
        Parent root = loader.load();
        SelectionController controller = loader.getController();
        controller.loadTeamForEditing(selected);
        ((Node) event.getSource()).getScene().setRoot(root);
    }

    @FXML private void onPlay(ActionEvent event) { /* Logique de jeu */ }

    @FXML
    private void onRename(ActionEvent event) {
        TeamBugemon selected = teamListView.getSelectionModel().getSelectedItem();
        TextInputDialog d = new TextInputDialog(selected.getName());
        d.showAndWait().ifPresent(n -> { if(!n.trim().isEmpty()) { selected.setName(n.trim()); teamListView.refresh(); }});
    }

    @FXML
    private void onDelete(ActionEvent event) {
        TeamBugemon selected = teamListView.getSelectionModel().getSelectedItem();
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer « " + selected.getName() + " » ?");
        if (a.showAndWait().get() == ButtonType.OK) teams.remove(selected);
    }

    @FXML
    private void onNewTeam(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("selectionview.fxml"));
        ((Node) event.getSource()).getScene().setRoot(root);
    }
}