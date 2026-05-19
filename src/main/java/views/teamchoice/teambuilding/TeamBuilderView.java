package views.teamchoice.teambuilding;

import configuration.AppConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.stage.StageStyle;
import models.bugemon.dto.BugemonDTO;
import models.bugemon.dto.TeamBugemonDTO;
import views.View;
import views.listener.TeamBuilderListener;

import java.io.IOException;
import java.util.List;

/**
 * Main view for the Bugémon selection screen.
 * Coordinates the two sub-views: {@link BugemonGridSubView} and {@link TeamPanelSubView}.
 * Sub-views are loaded via {@code fx:include} in {@code selectionview.fxml}.
 *
 * <p>
 * This view does not know which controller is using it: it only knows a
 * {@link TeamBuilderListener}. The same view instance can therefore be shared
 * between a creation controller and an edition controller, each one registering
 * itself as the listener when it takes over.
 * </p>
 */
public class TeamBuilderView extends View {

    private TeamBuilderListener listener;

    @FXML private BugemonGridSubView bugemonGridController;
    @FXML private TeamPanelSubView teamPanelController;

    /**
     * Loads the main FXML which includes bugemon_grid.fxml and team_panel.fxml.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public TeamBuilderView() throws IOException {
        super(AppConfig.FXML_SELECTION);
        root.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY && listener != null) {
                listener.onReset();
            }
        });
    }

    /**
     * Called automatically by JavaFX after all FXML fields are injected,
     * including sub-controllers from {@code fx:include}.
     */
    @FXML
    public void initialize() {
        // Sub-controllers are now injected and safe to use
    }

    /**
     * Registers the listener that will receive user events.
     * Propagates it to both sub-views.
     * Must be called after FXML loading and before showing the view.
     *
     * @param listener the listener to wire (typically the active controller)
     */
    public void setListener(TeamBuilderListener listener) {
        this.listener = listener;
        bugemonGridController.setListener(listener);
        teamPanelController.setListener(listener);
    }

    /**
     * Pre-selects the Bugémons of an existing team in the grid
     * and pushes the team list to the team panel.
     *
     * @param team the team to pre-select
     */
    public void preSelectTeam(TeamBugemonDTO team) throws IOException {
        bugemonGridController.preSelectTeam(team.getTeamDTO(), teamPanelController);
        teamPanelController.refreshTeamGrid(team.getTeamDTO());
    }

    /**
     * Resets both sub-views to their initial state.
     *
     * @param allBugemons the full list of Bugémons to reload in the grid
     */
    public void reset(List<BugemonDTO> allBugemons) throws IOException {
        bugemonGridController.reset(allBugemons);
        teamPanelController.reset();
    }

    /**
     * Selects a button visually in the grid.
     *
     * @param btn     the button to select
     * @param bugemon the Bugémon associated with the button
     */
    public void selectButton(Button btn, BugemonDTO bugemon) {
        bugemonGridController.selectButton(btn, bugemon);
    }

    /**
     * Deselects a button visually in the grid.
     *
     * @param btn the button to deselect
     */
    public void deselectButton(Button btn) {
        bugemonGridController.deselectButton(btn);
    }

    /**
     * Notifies the team panel that a Bugémon was added.
     *
     * @param bugemon the Bugémon that was added
     */
    public void onBugemonAdded(BugemonDTO bugemon) {
        teamPanelController.onBugemonAdded(bugemon);
    }

    /**
     * Notifies the team panel that a Bugémon was removed.
     *
     * @param bugemon the Bugémon that was removed
     */
    public void onBugemonRemoved(BugemonDTO bugemon) {
        teamPanelController.onBugemonRemoved(bugemon);
    }

    /**
     * Updates the stats label in the team panel.
     *
     * @param bugemon the Bugémon whose stats to display
     */
    public void showBugemonStats(BugemonDTO bugemon) {
        teamPanelController.showBugemonStats(bugemon);
    }

    /**
     * Pushes an updated team list to the team panel grid.
     * Called by the controller after each team modification.
     *
     * @param team the updated list of Bugémons to display
     */
    public void refreshTeamGrid(List<BugemonDTO> team) throws IOException {
        teamPanelController.refreshTeamGrid(team);
    }

    /**
     * Shows a team full error dialog in the team panel.
     */
    public void showTeamFullError() {
        teamPanelController.showError("Equipe pleine",
                "Vous ne pouvez sélectionner que 6 Bugémons maximum.");
    }

    /**
     * Toggles edit mode in the team panel.
     *
     * @param editing {@code true} if editing an existing team
     */
    public void setEditMode(boolean editing) {
        teamPanelController.setEditMode(editing);
    }

    @Override
    protected String getTitle() {
        return "Sélection d'équipe - Bugémon";
    }

    /**
     * Displays an error dialog.
     *
     * @param title the title of the dialog
     * @param message the error message displayed to the user
     */
    public void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setGraphic(null);
        alert.showAndWait();
    }
}