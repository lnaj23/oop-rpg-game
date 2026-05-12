package services.controller.dataprovider;

import exceptions.*;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import repositories.TeamRepository;

import java.util.List;

public class TeamProvider {

    private final BugemonProvider bugemonProvider = new BugemonProvider();

    public TeamProvider() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    public List<TeamBugemon> loadTeams()
            throws LoadingTeamFailedException, AttackNotFoundException, JsonResourceNotFoundException {

        List<TeamBugemon> teams = TeamRepository.getInstance().loadTeams();

        for (TeamBugemon team : teams) {
            team.ensureId();

            for (Bugemon bugemon : team.getTeam()) {
                bugemonProvider.initBugemonFromSavedTeam(bugemon);
            }
        }

        return teams;
    }

    public TeamBugemon findTeamById(String teamId) throws LoadingTeamFailedException {
        try {
            for (TeamBugemon team : loadTeams()) {
                if (team.getId().equals(teamId)) {
                    return new TeamBugemon(team);
                }
            }

            throw new LoadingTeamFailedException("Team not found: " + teamId, null);

        } catch (AttackNotFoundException | JsonResourceNotFoundException e) {
            throw new LoadingTeamFailedException("Error loading team: " + teamId, e);
        }
    }

    public void addTeam(TeamBugemon team) throws SavingTeamFailedException {
        List<TeamBugemon> teams = loadTeamsForMutation();

        team.ensureId();

        if (isNameTaken(teams, team.getName(), null)) {
            throw new IllegalArgumentException("Une équipe avec ce nom existe déjà.");
        }

        teams.add(team);
        TeamRepository.getInstance().saveTeams(teams);
    }

    public void updateTeamById(String teamId, TeamBugemon updatedTeam) throws SavingTeamFailedException {
        List<TeamBugemon> teams = loadTeamsForMutation();

        for (int i = 0; i < teams.size(); i++) {
            if (teams.get(i).getId().equals(teamId)) {
                updatedTeam.ensureId();
                updatedTeam.setName(teams.get(i).getName());
                teams.set(i, updatedTeam);
                TeamRepository.getInstance().saveTeams(teams);
                return;
            }
        }

        throw new SavingTeamFailedException("Team not found: " + teamId, null);
    }

    public void renameTeam(String teamId, String newName) throws SavingTeamFailedException {
        List<TeamBugemon> teams = loadTeamsForMutation();

        if (isNameTaken(teams, newName, teamId)) {
            throw new IllegalArgumentException("Une équipe avec ce nom existe déjà.");
        }

        for (TeamBugemon team : teams) {
            if (team.getId().equals(teamId)) {
                team.setName(newName);
                TeamRepository.getInstance().saveTeams(teams);
                return;
            }
        }

        throw new SavingTeamFailedException("Team not found: " + teamId, null);
    }

    public void deleteTeam(String teamId) throws SavingTeamFailedException {
        List<TeamBugemon> teams = loadTeamsForMutation();

        teams.removeIf(team -> team.getId().equals(teamId));

        TeamRepository.getInstance().saveTeams(teams);
    }

    private List<TeamBugemon> loadTeamsForMutation() throws SavingTeamFailedException {
        try {
            return loadTeams();
        } catch (LoadingTeamFailedException | AttackNotFoundException | JsonResourceNotFoundException e) {
            throw new SavingTeamFailedException("Error loading teams before saving", e);
        }
    }

    private boolean isNameTaken(List<TeamBugemon> teams, String name, String ignoredTeamId) {
        if (name == null) return false;

        String cleanedName = name.trim();

        for (TeamBugemon team : teams) {
            boolean sameName = team.getName() != null && team.getName().trim().equalsIgnoreCase(cleanedName);

            boolean sameTeam = ignoredTeamId != null && team.getId().equals(ignoredTeamId);

            if (sameName && !sameTeam) {
                return true;
            }
        }

        return false;
    }
}