package controllers.tower;

import controllers.Controller;
import controllers.ControllerManager;
import models.bugemon.Attack;
import models.bugemon.Bugemon;
import views.tower.SelectAttackView;

import java.io.IOException;

/**
 * Controller for the "select which attack to replace" sub-screen, used when
 * a Bugémon learns a new attack and already has the maximum number of attacks.
 * <p>
 * 1:1 binding with {@link SelectAttackView}.
 * </p>
 */
public class SelectAttackController extends Controller<SelectAttackView> {

    /** The Bugémon whose attack list is being modified. */
    private final Bugemon target;

    /** The new attack that will replace the selected one. */
    private final Attack newAttack;

    /**
     * Creates the controller, wires itself to the view, and populates
     * the attack buttons.
     *
     * @param metaController the central controller handling navigation
     * @param target         the Bugémon learning the new attack
     * @param newAttack      the new attack to teach
     * @throws IOException if the FXML view cannot be loaded
     */
    public SelectAttackController(ControllerManager metaController, Bugemon target, Attack newAttack) throws IOException {
        super(metaController, new SelectAttackView());
        this.target = target;
        this.newAttack = newAttack;
        this.view.setController(this);
        this.view.displayAttacks(target.getAttacks(), newAttack.getName());
    }

    /**
     * Replaces the chosen attack with the new one and advances the tower flow.
     */
    public void onAttackSelectedToReplace(int attackIndex) {
        Attack attackToReplace = target.getAttackFromIndex(attackIndex);
        target.replaceAttack(newAttack, attackToReplace);
        view.closeWindow();
        controllerManager.goToNextTowerStep();
    }

    /**
     * Cancels the attack replacement: closes the sub-window without modifying
     * the Bugémon. The tower step is not advanced.
     */
    public void onCancelClicked() {
        view.closeWindow();
    }

    /** @return the view managed by this controller */
    public SelectAttackView getView() {
        return view;
    }
}