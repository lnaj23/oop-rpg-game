package ulb.models.team;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ulb.models.Bugemon.TeamBugemon;

import java.util.UUID;

public class PreBuildTeamService {
    private final ObservableList<TeamBugemon> teams = FXCollections.observableArrayList();

    public void deleteTeam(TeamBugemon teamToDelete) {
        teams.remove(teamToDelete);
    }

    public void renameTeam(Team team, String newName) {
        team.setName(newName);
    }

    public void InitFakeTeam() {
        TeamBugemon firstTeam = new TeamBugemon();
        TeamBugemon secondTeam = new TeamBugemon();
        firstTeam.setName("équipe 1");
        secondTeam.setName("équipe 2");
        teams.add(firstTeam);
        teams.add(secondTeam);
    }

<<<<<<< HEAD
    //Getters
    public ObservableList<TeamBugemon> getTeams() {
=======
    // Getters
    public ObservableList<Team> getTeams() {
>>>>>>> 725a215 (Permet de modifer le nom d'une équipe en plus de pouvoir la supprimer)
        return teams;
    }
}
