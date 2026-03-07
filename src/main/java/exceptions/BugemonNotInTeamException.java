package exceptions;

/**
 * Thrown when a Bugémon is not found in the expected team.
 */
public class BugemonNotInTeamException extends RuntimeException {

    /**
     * @param id the ID of the Bugémon that was not found in the team
     */
    public BugemonNotInTeamException(String id) {
        super("Bugemon not found in the team : " + id);
    }
}