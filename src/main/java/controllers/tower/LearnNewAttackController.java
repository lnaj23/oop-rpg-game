package controllers.tower;

import controllers.Controller;
import controllers.ControllerManager;
import exceptions.BugemonNotInTeamException;
import javafx.application.Platform;
import models.bugemon.Attack;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import services.game.GameStateHolder;
import views.SelectBugemonFromTeamView;
import views.listener.SelectAttackListener;
import views.listener.SelectBugemonFromTeamListener;
import views.tower.SelectAttackView;

import java.io.IOException;

/**
 * Controller for the "learn new attack" reward.
 * <p>
 * First step: the player selects which Bugémon will learn the attack.
 * Second step: the player selects which existing attack to replace.
 * </p>
 */
public class LearnNewAttackController extends Controller<SelectBugemonFromTeamView>
        implements SelectBugemonFromTeamListener, SelectAttackListener {

    /** The new attack to teach to the selected Bugémon. */
    private Attack newAttack;

    /** The Bugémon selected in step 1, kept for step 2. */
    private Bugemon selectedBugemon;

    /** The attack selection view, kept to close it after selection. */
    private SelectAttackView attackView;

    /**
     * Creates the controller and wires the listener to the view.
     *
     * @param metaController the central controller handling navigation
     * @throws IOException if the FXML view cannot be loaded
     */
    public LearnNewAttackController(ControllerManager metaController) throws IOException {
        super(metaController, new SelectBugemonFromTeamView());
        this.view.setController(this);
        this.view.setListener(this);
    }

    /**
     * Sets the new attack that will replace an existing one.
     *
     * @param attack the new {@link Attack} to learn
     */
    public void setNewAttack(Attack attack) {
        this.newAttack = attack;
    }

    /**
     * Stores a reference to the attack selection view so it can be closed later.
     *
     * @param attackView the attack selection view
     */
    public void setAttackView(SelectAttackView attackView) {
        this.attackView = attackView;
    }

    /**
     * Displays the player's team in the sub-window.
     *
     * @throws IOException if the team display fails
     */
    public void displayTeam() throws IOException {
        this.view.displayTeam(
                GameStateHolder.getInstance().getCurrentTeamPlayer().getTeamDTO(),
                true
        );
    }

    /**
     * Step 1: the player selects a Bugémon.
     * Closes the team selection window and opens the attack selection window.
     *
     * @param bugemon the selected Bugémon (as DTO)
     */
    @Override
    public void onBugemonClickedRequest(BugemonDTO bugemon) {
        try {
            selectedBugemon = GameStateHolder.getInstance()
                    .getCurrentTeamPlayer()
                    .getBugemonById(bugemon.getId());
            view.closeWindow();
            Platform.runLater(() ->
                    controllerManager.openAttackSelectionWindow(selectedBugemon, newAttack, this)
            );
        } catch (BugemonNotInTeamException e) {
            view.showMessageKO("Erreur", "Ce Bugémon n'est pas dans votre équipe !");
        }
    }

    /**
     * Step 2: the player selects which attack to replace.
     * Uses the existing {@link Bugemon#replaceAttack} method which updates
     * both the attack object, the attack name, and the attack color.
     *
     * @param attackIndex index of the attack to replace
     */
    @Override
    public void onAttackSelectedToReplace(int attackIndex) {
        Attack attackToReplace = selectedBugemon.getAttackFromIndex(attackIndex);
        selectedBugemon.replaceAttack(newAttack, attackToReplace);
        attackView.closeWindow();
        controllerManager.goToNextTowerStep();
    }

    /**
     * Handles cancel from the Bugémon selection screen.
     */
    @Override
    public void onCancelClickedRequest() {
        view.closeWindow();
    }

    /**
     * Handles cancel from the attack selection screen.
     */
    @Override
    public void onCancelClicked() {
        attackView.closeWindow();
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