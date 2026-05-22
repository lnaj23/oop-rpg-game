package services.controller.prebuiltteam;

import exceptions.*;
import models.bugemon.Bugemon;
import models.bugemon.TeamBugemon;
import repositories.TeamRepository;
import services.controller.dataprovider.BugemonProvider;

import java.util.List;

/**
 * Application service for managing saved teams: loading and fully initialising
 * them, creating, updating, renaming and deleting, with name-uniqueness checks.
 * Read accessors return defensive copies.
 */
public class TeamService {

    private final BugemonProvider bugemonProvider = new BugemonProvider();

    public TeamService() throws AttackNotFoundException, JsonResourceNotFoundException {
    }

    /**
     * Loads all teams and fully initializes their Bugémons (attacks, runtime state).
     */
    public List<TeamBugemon> loadTeams() throws LoadingTeamFailedException, AttackNotFoundException, JsonResourceNotFoundException {

        List<TeamBugemon> teams = TeamRepository.getInstance().loadTeams();

        for (TeamBugemon team : teams) {
            team.ensureId();
            for (Bugemon bugemon : team.getTeam()) {
                bugemonProvider.initBugemonFromSavedTeam(bugemon);
            }
        }

        return teams;
    }

    /**
     * Returns a defensive copy of the team with the given id.
     */
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

    /**
     * Adds a new team. Validates that its name is not already used.
     */
    public void addTeam(TeamBugemon team) throws SavingTeamFailedException {
        team.ensureId();
        List<TeamBugemon> teams = loadTeamsForMutation();

        if (isNameTaken(teams, team.getName(), null)) {
            throw new IllegalArgumentException("Une équipe avec ce nom existe déjà.");
        }

        try {
            TeamRepository.getInstance().addTeam(team);
        } catch (LoadingTeamFailedException e) {
            throw new SavingTeamFailedException("Error loading teams before saving", e);
        }
    }

    /**
     * Replaces the team with the given id, preserving its original name.
     */
    public void updateTeamById(String teamId, TeamBugemon updatedTeam) throws SavingTeamFailedException {
        try {
            TeamBugemon existing = findTeamById(teamId);
            updatedTeam.ensureId();
            updatedTeam.setName(existing.getName());
            TeamRepository.getInstance().updateTeam(updatedTeam);
        } catch (LoadingTeamFailedException e) {
            throw new SavingTeamFailedException("Error loading team: " + teamId, e);
        }
    }

    /**
     * Renames a team. Validates that the new name is not already used by another team.
     */
    public void renameTeam(String teamId, String newName) throws SavingTeamFailedException {
        List<TeamBugemon> teams = loadTeamsForMutation();

        if (isNameTaken(teams, newName, teamId)) {
            throw new IllegalArgumentException("Une équipe avec ce nom existe déjà.");
        }

        try {
            TeamRepository.getInstance().renameTeam(teamId, newName);
        } catch (LoadingTeamFailedException e) {
            throw new SavingTeamFailedException("Error loading teams before saving", e);
        }
    }

    public void deleteTeam(String teamId) throws SavingTeamFailedException {
        try {
            TeamRepository.getInstance().deleteTeam(teamId);
        } catch (LoadingTeamFailedException e) {
            throw new SavingTeamFailedException("Error loading teams before saving", e);
        }
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
            boolean sameName = team.getName() != null
                    && team.getName().trim().equalsIgnoreCase(cleanedName);
            boolean sameTeam = ignoredTeamId != null
                    && team.getId().equals(ignoredTeamId);
            if (sameName && !sameTeam) return true;
        }
        return false;
    }
}