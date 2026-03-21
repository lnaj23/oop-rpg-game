package views.teamchoice.selection;

import controllers.teamchoice.SelectionController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import models.bugemon.dto.BugemonDTO;
import models.bugemon.dto.TeamBugemonDTO;
import views.View;

import java.io.IOException;
import java.util.List;

/**
 * Main view for the Bugémon selection screen.
 * Coordinates the two sub-views: BugemonGridView and TeamPanelView.
 * Sub-views are loaded via fx:include in selectionview.fxml.
 * JavaFX injects sub-controllers as bugemonGridController and teamPanelController
 * after FXML loading, following the fx:id + "Controller" naming convention.
 */
public class SelectionView extends View<SelectionController> {

    @FXML private BugemonGridSubView bugemonGridController;
    @FXML private TeamPanelSubView teamPanelController;

    /**
     * Loads the main FXML which includes bugemon_grid.fxml and team_panel.fxml.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public SelectionView() throws IOException {
        super("/fxml/teamchoice/selection/selectionview.fxml");
        //right click to unselect every bugemon
        root.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                controller.reset();
            }
        });
    }

    /**
     * Called automatically by JavaFX after all FXML fields are injected,
     * including sub-controllers from fx:include.
     * This is the correct place to propagate the controller to sub-views,
     * since bugemonGridController and teamPanelController are null before this point.
     */
    @FXML
    public void initialize() {
        // Sub-controllers are now injected and safe to use
    }

    /**
     * Propagates the controller to both sub-views.
     * Must be called after initialize() has run, i.e. after FXML is fully loaded.
     *
     * @param controller the selection controller
     */
    @Override
    public void setController(SelectionController controller) {
        super.setController(controller);
        bugemonGridController.setController(controller);
        teamPanelController.setController(controller);
    }

    /**
     * Populates the grid with all available Bugémons.
     *
     * @param allBugemons the full list of Bugémons to display
     */
    public void displayBugemons(List<BugemonDTO> allBugemons) {
        bugemonGridController.displayBugemons(allBugemons);
    }

    /**
     * Pre-selects the Bugémons of an existing team in the grid.
     *
     * @param team the team to pre-select
     */
    public void preSelectTeam(TeamBugemonDTO team) {
        bugemonGridController.preSelectTeam(team.getTeamDTO(), teamPanelController);
        teamPanelController.refreshTeamGrid();
    }

    /**
     * Resets both sub-views to their initial state.
     *
     * @param allBugemons the full list of Bugémons to reload in the grid
     */
    public void reset(List<BugemonDTO> allBugemons) {
        bugemonGridController.reset(allBugemons);
        teamPanelController.reset();
    }

    /**
     * Selects a button visually in the grid.
     *
     * @param btn the button to select
     */
    public void selectButton(Button btn,BugemonDTO bugemon) {
        bugemonGridController.selectButton(btn,bugemon);
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
     * Shows a team full error dialog in the team panel.
     */
    public void showTeamFullError() {
        teamPanelController.showError("Equipe pleine",
                "Vous ne pouvez sélectionner que 6 Bugémons maximum.");
    }

    public void setEditMode(boolean editing) {
        teamPanelController.setEditMode(editing);
    }

    @Override
    protected String getTitle() {
        return "Sélection d'équipe - Bugémon";
    }
}