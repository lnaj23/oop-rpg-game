package models.Bugemon.DTO;

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
     * Adds a Bugémon to the team.
     *
     * @param bug the Bugémon to add, cast internally to {@link models.Bugemon.Bugemon}
     */
    void addBugemonDTO(BugemonDTO bug);

    /**
     * Removes a Bugémon from the team.
     *
     * @param bug the Bugémon to remove
     */
    void removeBugemonDTO(BugemonDTO bug);
}