package models.bugemon.dto;

import java.util.List;

/**
 * Read-only interface exposing the display data of a team.
 * Passed to views so they can read team data without modifying the model.
 */
public interface TeamBugemonDTO {

    /**
     * @return the name of the team
     */
    String getName();

    /**
     * @return the list of Bugémons in the team
     */
    List<BugemonDTO> getTeamDTO();

    /**
     * @return the number of Bugémons in the team
     */
    int getSize();

    /**
     * @return the Bugémon currently on the field (read-only view)
     */
    BugemonDTO getOnFieldBugemon();

}