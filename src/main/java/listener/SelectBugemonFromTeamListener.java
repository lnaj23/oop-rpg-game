package listener;

import models.bugemon.dto.BugemonDTO;
public interface SelectBugemonFromTeamListener {
    void onBugemonClickedRequest(BugemonDTO bugemon);
    void onCancelClickedRequest();
}
