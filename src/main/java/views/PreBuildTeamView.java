package views;

import controllers.PreBuildTeamController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Bugemon.Bugemon;
import models.Bugemon.TeamBugemon;

import java.io.IOException;
import java.util.Optional;

/**
 * View for the team management screen.
 * Displays the list of saved teams and their Bugémons, and provides actions to play, modify, rename, or delete them.
 */
public class PreBuildTeamView extends View {

    private PreBuildTeamController controller;

    @FXML private ListView<TeamBugemon> teamListView;
    @FXML private ListView<String> bugemonListView;
    @FXML private Button btnRetour;
    @FXML private Button btnNewTeam;
    @FXML private Button btnPlay;
    @FXML private Button btnModify;
    @FXML private Button btnRename;
    @FXML private Button btnDelete;

    /**
     * Loads the FXML, binds the team list, and wires all button actions.
     * Action buttons (play, modify, rename, delete) are disabled when no team is selected.
     *
     * @param teams the shared observable list of saved teams to display
     * @throws IOException if the FXML file cannot be loaded
     */
    public PreBuildTeamView(ObservableList<TeamBugemon> teams) throws IOException {
        super("/fxml/prebuildteam.fxml");

        teamListView.setItems(teams);

        btnPlay.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());
        btnModify.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());
        btnRename.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());

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

        btnRetour.setOnAction(e -> this.controller.goToMenu());
        btnNewTeam.setOnAction(e -> this.controller.goToNewTeam());
        btnPlay.setOnAction(e -> this.controller.play(getSelected()));
        btnModify.setOnAction(e -> this.controller.modifyTeam(getSelected()));
        btnRename.setOnAction(e -> onRenameClicked());
        btnDelete.setOnAction(e -> onDeleteClicked());
    }

    /**
     * @param controller the controller to delegate actions to
     */
    public void setController(PreBuildTeamController controller) {
        this.controller = controller;
    }

    /**
     * @return the currently selected team, or null if none is selected
     */
    private TeamBugemon getSelected() {
        return teamListView.getSelectionModel().getSelectedItem();
    }

    /**
     * Shows a dialog to rename the selected team and delegates to the controller.
     */
    private void onRenameClicked() {
        TeamBugemon selected = getSelected();
        if (selected == null) return;
        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle("Renommer l'équipe");
        dialog.setHeaderText("Nouveau nom pour « " + selected.getName() + " »");
        dialog.setContentText("Nom :");
        dialog.showAndWait().ifPresent(newName -> {
            if (!newName.trim().isEmpty()) {
                this.controller.renameTeam(selected, newName.trim());
            }
        });
    }

    /**
     * Shows a confirmation dialog before deleting the selected team.
     */
    private void onDeleteClicked() {
        TeamBugemon selected = getSelected();
        if (selected == null) return;
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Supprimer l'équipe");
        confirm.setHeaderText("Supprimer « " + selected.getName() + " » ?");
        confirm.setContentText("Cette action est irréversible.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            this.controller.deleteTeam(selected);
            bugemonListView.getItems().clear();
        }
    }

    /** Forces the team list to re-render its cells (e.g. after a rename). */
    public void refreshTeamList() {
        teamListView.refresh();
    }

    @Override
    protected String getTitle() {
        return "Mes équipes - Bugémon";
    }
}