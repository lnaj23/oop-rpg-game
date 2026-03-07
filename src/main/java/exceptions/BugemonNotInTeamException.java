package exceptions;

public class BugemonNotInTeamException extends RuntimeException {
    public BugemonNotInTeamException(String id) {
        super("Bugemon not found in the team : " + id);
    }
}
