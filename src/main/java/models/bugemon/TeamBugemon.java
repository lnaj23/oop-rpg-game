package models.bugemon;

import models.bugemon.dto.BugemonDTO;
import models.bugemon.dto.TeamBugemonDTO;
import exceptions.BugemonNotInTeamException;
import services.dataprovider.BugemonProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a named team of up to 6 Bugémons.
 * Implements {@link TeamBugemonDTO} to expose read-only data to views.
 */
public class TeamBugemon implements TeamBugemonDTO {

    /** Display name of this team. */
    private String name = null;

    /** List of Bugémons belonging to this team. */
    private final List<Bugemon> team;

    /** The Bugémon currently active on the battlefield, or null if none. */
    private Bugemon onFieldBugemon = null;

    /** No-arg constructor. Initialises an empty team. */
    public TeamBugemon() {
        team = new ArrayList<>();
    }

    /**
     * Constructs a team from a list of Bugémon IDs.
     *
     * @param listeOfId the list of Bugémon IDs to load
     */
    public TeamBugemon(List<String> listeOfId) {
        this.team = BugemonProvider.getListBugemon(listeOfId);
    }

    /**
     * Constructs a named team from a list of Bugémon IDs.
     *
     * @param name   the name of the team
     * @param listeOfId the list of Bugémon IDs to load
     */
    public TeamBugemon(String name, List<String> listeOfId) {
        this.name = name;
        this.team = BugemonProvider.getListBugemon(listeOfId);
    }

    /**
     * Copy constructor. Creates a deep copy of the given team.
     * The on-field Bugémon is reset to null.
     *
     * @param other the team to copy
     */
    public TeamBugemon(TeamBugemon other) {
        this.name           = other.name;
        this.team           = other.team.stream().map(Bugemon::new).toList();
        this.onFieldBugemon = null; // pas de bugemon sur le terrain au début
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<BugemonDTO> getTeamDTO() {
        return new ArrayList<>(team);
    }

    @Override
    public int getSize() {
        return team.size();
    }

    /**
     * Returns the list of Bugémons in this team.
     *
     * @return the team list
     */
    public List<Bugemon> getTeam() {
        return team;
    }

    /**
     * Returns the Bugémon currently active on the battlefield.
     *
     * @return the on-field Bugémon, or null if none
     */
    public Bugemon getOnFieldBugemon() {
        return onFieldBugemon;
    }

    /**
     * Returns all attacks from every Bugémon in the team.
     *
     * @return a flat list of all attacks
     */
    public List<Attack> getAllAttacks() {
        List<Attack> allAttacks = new ArrayList<>();
        for (Bugemon bug : team) {
            allAttacks.addAll(bug.getAttacks());
        }
        return allAttacks;
    }

    /**
     * Returns the attacks of the Bugémon with the given ID.
     *
     * @param bugemonId the ID of the Bugémon
     * @return the list of attacks of that Bugémon
     * @throws BugemonNotInTeamException if no Bugémon with that ID is found
     */
    public List<Attack> getAttacksOf(String bugemonId) {
        for (Bugemon bug : team) {
            if (bug.getId().equals(bugemonId)) {
                return bug.getAttacks();
            }
        }
        throw new BugemonNotInTeamException(bugemonId);
    }

    /**
     * Returns the Bugémons in this team that are still able to fight.
     *
     * @return list of Bugémons with HP greater than 0
     */
    public List<Bugemon> getAvailableBugemons() {
        List<Bugemon> availableBugemons = new ArrayList<>();
        for (Bugemon bug : team) {
            if (bug.isStillStanding()) {
                availableBugemons.add(bug);
            }
        }
        return availableBugemons;
    }

    /**
     * Sets the display name of this team.
     *
     * @param name the new team name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a Bugémon to this team.
     *
     * @param bug the Bugémon to add
     */
    public void addBugemon(Bugemon bug) {
        team.add(bug);
    }

    @Override
    public void addBugemonDTO(BugemonDTO bug) {
        team.add((Bugemon) bug);
    }

    @Override
    public void removeBugemonDTO(BugemonDTO bug) {
        team.removeIf(b -> b.getId().equals(bug.getId()));
    }

    /**
     * Sets the Bugémon currently active on the battlefield.
     * The Bugémon must be present in the team.
     *
     * @param bug the Bugémon to send on the field
     * @throws BugemonNotInTeamException if the Bugémon is not found in the team
     */
    public void changeOnFieldBugemon(Bugemon bug) {
        for (Bugemon bug2 : team) {
            if (bug2.getId().equals(bug.getId())) {
                onFieldBugemon = bug2;
                return;
            }
        }
        throw new BugemonNotInTeamException(bug.getId());
    }

    public boolean isOnFieldBugemonKO() {
        return onFieldBugemon.isKO();
    }

    public boolean isTeamStillStanding() {
        return team.stream().anyMatch(Bugemon::isStillStanding);
    }

    /**
     * Returns whether any attack in the team is null.
     *
     * @return true if at least one attack is null
     */
    public boolean hasNullAttacks() {
        for (Attack attack : getAllAttacks()) {
            if (attack == null) return true;
        }
        return false;
    }
}