package views.listener;

import models.bugemon.dto.BugemonDTO;

/**
 * Listener interface for managing Bugemon selection from a team.
 *
 * This interface defines callbacks for user interactions when selecting
 * a Bugemon in the team interface.
 */
public interface SelectBugemonFromTeamListener {

    /**
     * Called when the user clicks on a Bugemon.
     *
     * @param bugemon the Bugemon selected by the user
     */
    void onBugemonClickedRequest(BugemonDTO bugemon);

    /**
     * Called when the user clicks the cancel button.
     */
    void onCancelClickedRequest();
}
