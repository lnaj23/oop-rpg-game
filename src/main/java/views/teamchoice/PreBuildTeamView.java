package views.teamchoice;

import configuration.AppConfig;
import controllers.teamchoice.PreBuildTeamController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.StageStyle;
import models.bugemon.dto.TeamBugemonDTO;
import views.View;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * View for the team management screen.
 * Displays the list of saved teams and their Bugémons, and provides actions to play, modify, rename, or delete them.
 */
public class PreBuildTeamView extends View<PreBuildTeamController> {

    @FXML private ListView<TeamBugemonDTO> teamListView;
    @FXML private ListView<String> bugemonListView;
    @FXML private Button btnRetour;
    @FXML private Button btnNewTeam;
    @FXML private Button btnPlay;
    @FXML private Button btnModify;
    @FXML private Button btnRename;
    @FXML private Button btnDelete;

    /**
     * Loads the FXML and wires all button actions.
     * Action buttons (play, modify, rename, delete) are disabled when no team is selected.
     * Call {@link #refreshTeams(List)} after construction to populate the list.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public PreBuildTeamView() throws IOException {
        super(AppConfig.FXML_PREBUILDTEAM);
        binButtons();
        initTeamListView();
        setActionButtons();
    }

    /**
     * Initializes the team list cell factory and sets up the selection listener
     * to update the Bugémon list when a team is selected.
     */
    private void initTeamListView() {
        teamListView.setCellFactory(lv -> new ListCell<TeamBugemonDTO>() {
            @Override
            protected void updateItem(TeamBugemonDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getName() + " (" + item.getSize() + " Bugémon(s))");
            }
        });

        teamListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            bugemonListView.getItems().clear();
            if (newV != null)
                newV.getTeamDTO().forEach(b -> bugemonListView.getItems().add(b.getName() + " (Niveau " + b.getLevel() + ")"));
        });
    }

    /**
     * Binds the action buttons' disabled state to the team list selection.
     * Buttons are disabled when no team is selected.
     */
    private void binButtons() {
        btnPlay.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());
        btnModify.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());
        btnRename.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());
        btnDelete.disableProperty().bind(teamListView.getSelectionModel().selectedItemProperty().isNull());
    }

    /**
     * Wires all button actions to their corresponding controller methods.
     */
    private void setActionButtons(){
        btnRetour.setOnAction(e -> this.controller.goToMenu());
        btnNewTeam.setOnAction(e -> this.controller.goToNewTeam());
        btnPlay.setOnAction(e -> this.controller.play(getSelectedIndex()));
        btnModify.setOnAction(e -> this.controller.modifyTeam(getSelectedIndex()));
        btnRename.setOnAction(e -> onRenameClicked());
        btnDelete.setOnAction(e -> onDeleteClicked());
    }

    /**
     * Replaces the displayed team list with the given one.
     * Accepts any list whose elements implement {@link TeamBugemonDTO} (e.g. {@code List<TeamBugemon>}).
     *
     * @param teams the updated list of teams to display
     */
    public void refreshTeams(List<? extends TeamBugemonDTO> teams) {
        teamListView.getItems().setAll(teams);
    }

    /**
     * @return the currently selected team as a DTO, or null if none is selected
     */
    private int getSelectedIndex() {
        return teamListView.getSelectionModel().getSelectedIndex();
    }

    /**
     * Shows a dialog to rename the selected team and delegates to the controller.
     */
    private void onRenameClicked() {
        int index = getSelectedIndex();
        if (index < 0) return;
        TeamBugemonDTO selected = teamListView.getItems().get(index);
        showRenameDialog(selected).ifPresent(newName -> {
            if (!newName.trim().isEmpty())
                this.controller.renameTeam(index, newName.trim());
        });
    }

    /**
     * Builds and shows a text input dialog to rename a team.
     *
     * @param selected the team to rename
     * @return an Optional containing the new name entered by the user
     */
    private Optional<String> showRenameDialog(TeamBugemonDTO selected) {
        String title = "Renommer l'équipe";
        String headerText = "Nouveau nom pour « " + selected.getName() + " »";
        String contentText = "Nom :";
        return showAndWaitTextInputDialog(selected, title, headerText, contentText);
    }

    /**
     * Shows a confirmation dialog before deleting the selected team.
     */
    private void onDeleteClicked() {
        int index = getSelectedIndex();
        if (index < 0) return;
        TeamBugemonDTO selected = teamListView.getItems().get(index);
        Optional<ButtonType> result = showDeleteConfirmDialog(selected);
        if (result.isPresent() && result.get() == ButtonType.OK) {
            this.controller.deleteTeam(index);
            bugemonListView.getItems().clear();
        }
    }

    /**
     * Builds and shows a confirmation dialog for deleting a team.
     *
     * @param selected the team to delete
     * @return the user's response
     */
    private Optional<ButtonType> showDeleteConfirmDialog(TeamBugemonDTO selected) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Supprimer l'équipe");
        confirm.setHeaderText("Supprimer « " + selected.getName() + " » ?");
        confirm.setContentText("Cette action est irréversible.");
        confirm.initStyle(StageStyle.UNDECORATED);
        confirm.setGraphic(null);
        confirm.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(AppConfig.CSS_COMMON)).toExternalForm()
        );
        return confirm.showAndWait();
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