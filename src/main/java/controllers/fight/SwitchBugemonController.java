package controllers.fight;

import controllers.Controller;
import controllers.MetaController;
import models.bugemon.dto.BugemonDTO;
import views.fight.SwitchBugemonView;
import listener.SelectBugemonFromTeamListener;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Controller for the Bugemon switch sub-view used during a fight.
 * <p>
 * This controller initializes the {@link SwitchBugemonView}, displays the
 * player's team, validates switch choices, and delegates the selected Bugemon
 * through a callback. It also handles cancellation when switching is optional.
 * </p>
 */
public class SwitchBugemonController extends Controller<SwitchBugemonView> implements SelectBugemonFromTeamListener {

    private final Object currentOnFieldId;
    private final Consumer<Object> onBugemonSelected;
    private final Runnable onCancel;

    /**
     * Creates the controller for the Bugemon switch sub-view.
     *
     * @param metaController the central controller responsible for application navigation
     * @param team the player's team to display in the switch sub-view
     * @param onPurpose {@code true} if the player opened the switch window voluntarily,
     *                  {@code false} if the switch is forced
     * @param currentOnFieldId the identifier of the Bugemon currently on the field
     * @param onBugemonSelected callback executed when the user selects a valid Bugemon
     * @param onCancel callback executed when the user cancels the switch
     * @throws IOException if the switch sub-view cannot be loaded
     */
    public SwitchBugemonController(MetaController metaController,
                                   List<BugemonDTO> team,
                                   boolean onPurpose,
                                   Object currentOnFieldId,
                                   Consumer<Object> onBugemonSelected,
                                   Runnable onCancel) throws IOException {
        super(metaController, new SwitchBugemonView());
        this.currentOnFieldId = currentOnFieldId;
        this.onBugemonSelected = onBugemonSelected;
        this.onCancel = onCancel;

        this.view.setController(this);
        this.view.setListener(this);
        this.view.displayTeam(team, onPurpose);
    }

    /**
     * Handles the selection of a Bugemon in the switch sub-view.
     * <p>
     * Displays an error message if the selected Bugemon is KO or is already on the field.
     * Otherwise, closes the sub-window and forwards the selected Bugemon id through
     * the callback.
     * </p>
     *
     * @param bugemon the Bugemon selected by the user
     */
    public void onBugemonClicked(BugemonDTO bugemon) {
        if (bugemon.getcurrenPv() <= 0) {
            view.showMessageKO("Erreur", "Ce Bugemon est KO.");
            return;
        }

        if (bugemon.getId().equals(currentOnFieldId)) {
            view.showMessageKO("Erreur", "Ce Bugemon est deja sur le terrain !");
            return;
        }

        view.closeWindow();
        onBugemonSelected.accept(bugemon.getId());
    }

    /**
     * Handles the click on the cancel button by closing the sub-window
     * and executing the cancellation callback.
     */
    public void onCancelClicked() {
        view.closeWindow();
        onCancel.run();
    }

    /**
     * Implements the listener interface method for Bugemon selection.
     * Delegates the selection request to the internal handler.
     *
     * @param bugemon the Bugemon selected by the user
     */
    @Override
    public void onBugemonClickedRequest(BugemonDTO bugemon) {
        onBugemonClicked(bugemon);
    }

    /**
     * Implements the listener interface method for cancellation.
     * Delegates the cancellation request to the internal handler.
     */
    @Override
    public void onCancelClickedRequest() {
        onCancelClicked();
    }

    /**
     * Returns the view managed by this controller.
     *
     * @return the Bugemon switch sub-view
     */
    public SwitchBugemonView getView() {
        return view;
    }
}