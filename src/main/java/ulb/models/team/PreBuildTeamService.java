package ulb.models.team;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.UUID;

public class PreBuildTeamService {
    private final ObservableList<Team> teams = FXCollections.observableArrayList(
            team -> team.getObservables());

    public void deleteTeam(Team teamToDelete) {
        teams.remove(teamToDelete);
    }

    public void renameTeam(Team team, String newName) {
        team.setName(newName);
    }

    public void InitFakeTeam() {
        Team firstTeam = new Team("équipe 1", UUID.randomUUID());
        Team secondTeam = new Team("équipe 2", UUID.randomUUID());
        teams.add(firstTeam);
        teams.add(secondTeam);
    }

    // Getters
    public ObservableList<Team> getTeams() {
        return teams;
    }
}
