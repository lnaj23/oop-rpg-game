package views.listener;

import javafx.scene.control.Button;
import models.bugemon.dto.BugemonDTO;

/**
 * Listener used by the selection view to delegate user actions.
 *
 * <p>
 * This allows the same {@link views.teamchoice.selection.SelectionView} to be reused
 * by different controllers, for example one controller for team creation
 * ({@code SelectionController}) and another one for team edition ({@code EditTeamController}).
 * </p>
 *
 * <p>
 * The view does not know which concrete controller is wired behind the listener:
 * it only emits user events and pushes data through the view's public API.
 * </p>
 */
public interface SelectionListener {

    /**
     * Called when the user clicks a Bugémon in the grid.
     *
     * @param btn         the button that was clicked
     * @param bugemon     the Bugémon associated with the button
     * @param wasSelected {@code true} if the button was already selected before the click
     * @param teamFull    {@code true} if the team has already reached its maximum size
     */
    void onBugemonClicked(Button btn, BugemonDTO bugemon, boolean wasSelected, boolean teamFull);

    /**
     * Called when the user confirms the current selection.
     *
     * @param teamName the name typed by the user when creating a new team,
     *                 or {@code null} when editing an existing team
     */
    void onConfirm(String teamName);

    /** Called when the user clicks the Play button (creation flow only). */
    void onPlay();

    /** Called when the user clicks the Load Team button (creation flow only). */
    void onLoadTeam();

    /** Called when the user clicks the Back button (creation flow only). */
    void onBack();

    /** Called when the user right-clicks anywhere to reset the current selection. */
    void onReset();
}