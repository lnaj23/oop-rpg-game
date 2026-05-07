package controllers.tower;

import controllers.Controller;
import controllers.ControllerManager;
import exceptions.BugemonNotInTeamException;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import models.game.LevelUpBonus;
import services.game.GameStateHolder;
import views.SelectBugemonFromTeamView;
import views.listener.SelectBugemonFromTeamListener;

import java.io.IOException;

/**
 * Controller for applying a stat bonus reward to a Bugémon.
 * <p>
 * Opens a sub-window displaying the player's team. When the player
 * selects a Bugémon, the {@link LevelUpBonus} is applied permanently
 * to that Bugémon's stats, then the tower advances to the next step.
 * </p>
 */
public class ApplyBonusRewardController extends Controller<SelectBugemonFromTeamView> implements SelectBugemonFromTeamListener {

    /** The stat bonus to apply to the selected Bugémon. */
    private LevelUpBonus bonus;

    /**
     * Creates the controller and wires the listener to the view.
     *
     * @param metaController the central controller handling navigation
     * @throws IOException if the FXML view cannot be loaded
     */
    public ApplyBonusRewardController(ControllerManager metaController, LevelUpBonus bonus) throws IOException {
        super(metaController, new SelectBugemonFromTeamView());
        this.view.setController(this);
        this.view.setListener(this);
        this.bonus = bonus;
        this.view.displayTeam(GameStateHolder.getInstance().getCurrentTeamPlayer().getTeamDTO(), true);
    }

    /**
     * Displays the player's team in the sub-window.
     *
     * @throws IOException if the team display fails
     */
    public void displayTeam() throws IOException {
        this.view.displayTeam(GameStateHolder.getInstance().getCurrentTeamPlayer().getTeamDTO(), true);
    }

    /**
     * Handles the selection of a Bugémon by the player.
     * Applies the stat bonus and advances to the next tower step.
     *
     * @param bugemon the selected Bugémon (as DTO)
     */
    @Override
    public void onBugemonClickedRequest(BugemonDTO bugemon) {
        try {
            Bugemon bug = GameStateHolder.getInstance()
                    .getCurrentTeamPlayer()
                    .getBugemonById(bugemon.getId());
            bonus.applyTo(bug);
            view.closeWindow();
            controllerManager.goToNextTowerStep();
        } catch (BugemonNotInTeamException e) {
            view.showMessageKO("Erreur", "Ce Bugémon n'est pas dans votre équipe !");
        }
    }

    /**
     * Handles the cancel button by closing the sub-window
     * without applying any bonus.
     */
    @Override
    public void onCancelClickedRequest() {
        view.closeWindow();
    }

    /**
     * Returns the view managed by this controller.
     *
     * @return the team selection sub-view
     */
    public SelectBugemonFromTeamView getView() {
        return view;
    }
}