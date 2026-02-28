package ulb.models.team;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.UUID;

public class PreBuildTeamService {
    private final ObservableList<Team> teams = FXCollections.observableArrayList();

    public void deleteTeam(Team teamToDelete) {
        teams.remove(teamToDelete);
    }

    public void renameTeam(Team team, String newName) {
        team.setName(newName);
    }

    public void InitFakeTeam() {
        teams.add(new Team("1 équipe", UUID.randomUUID()));
        teams.add(new Team("2 équipe", UUID.randomUUID()));
    }

    // Getters
    public ObservableList<Team> getTeams() {
        return teams;
    }
}
