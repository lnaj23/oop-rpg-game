package models.bugemon;

import exceptions.BugemonNotInTeamException;
import models.bugemon.dto.BugemonDTO;
import models.bugemon.dto.TeamBugemonDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Represents a team of Bugémons.
 *
 * <p>
 * A {@code TeamBugemon} contains the Bugémons chosen by the player or generated
 * for an enemy. It also stores the Bugémon currently on the field during a fight.
 * </p>
 *
 * <p>
 * Each team has a stable {@code id}. This id is used to identify saved teams
 * inside the JSON file without relying on their position in the list. This is
 * safer than using an index, because the order of the list can change after
 * deletion, reload, sorting, or insertion.
 * </p>
 *
 * <p>
 * The {@code name} is only the display name shown to the user. It can be forced
 * to be unique by the service layer, but it should not be used as the technical
 * identifier of the team.
 * </p>
 */
public class TeamBugemon implements TeamBugemonDTO {

    /**
     * Stable technical identifier of the team.
     *
     * <p>
     * This id is stored in the JSON file and allows the application to find the
     * same saved team later, even if the list order changes.
     * </p>
     */
    private String id = UUID.randomUUID().toString();

    /**
     * Display name of the team.
     *
     * <p>
     * This name is shown in the interface. It is not the technical identifier
     * of the team.
     * </p>
     */
    private String name = null;

    /**
     * List of Bugémons belonging to this team.
     */
    private final List<Bugemon> team;

    /**
     * Bugémon currently active on the battlefield.
     *
     * <p>
     * This field is transient because it represents a temporary runtime state.
     * It should not be saved in the JSON file.
     * </p>
     */
    private transient Bugemon onFieldBugemon = null;

    /**
     * Creates an empty team with a generated stable id.
     */
    public TeamBugemon() {
        this.team = new ArrayList<>();
        ensureId();
    }

    /**
     * Creates a deep copy of another team.
     *
     * <p>
     * The team id is preserved because the copied object still represents the
     * same saved team. This is useful when editing or playing with an existing
     * saved team.
     * </p>
     *
     * <p>
     * The on-field Bugémon is reset because it is only a temporary fight state.
     * </p>
     *
     * @param other the team to copy
     */
    public TeamBugemon(TeamBugemon other) {
        this.id = other.id;
        this.name = other.name;
        this.team = other.team.stream()
                .map(Bugemon::new)
                .collect(Collectors.toList());
        this.onFieldBugemon = null;
        ensureId();
    }

    /**
     * Creates a team with a name and a list of Bugémons.
     *
     * @param name     the display name of the team
     * @param bugemons the Bugémons contained in the team
     */
    public TeamBugemon(String name, List<Bugemon> bugemons) {
        this.name = name;
        this.team = bugemons;
        ensureId();
    }

    /**
     * Creates an unnamed team with a list of Bugémons.
     *
     * @param bugemons the Bugémons contained in the team
     */
    public TeamBugemon(List<Bugemon> bugemons) {
        this.team = bugemons;
        ensureId();
    }

    /**
     * Ensures that the team has a valid id.
     *
     * <p>
     * This method is useful when loading old JSON data created before the
     * introduction of the {@code id} field. If the loaded team has no id, a new
     * one is generated.
     * </p>
     */
    public void ensureId() {
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
    }

    /**
     * Returns the stable technical id of the team.
     *
     * @return the team id
     */
    @Override
    public String getId() {
        ensureId();
        return id;
    }

    /**
     * Returns the display name of the team.
     *
     * @return the team name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns a read-only DTO view of the Bugémons in the team.
     *
     * <p>
     * The returned list is a new list, but the objects inside it are the same
     * Bugémon objects exposed as DTOs.
     * </p>
     *
     * @return the list of Bugémons as DTOs
     */
    @Override
    public List<BugemonDTO> getTeamDTO() {
        return new ArrayList<>(team);
    }

    /**
     * Returns the number of Bugémons in the team.
     *
     * @return the size of the team
     */
    @Override
    public int getSize() {
        return team.size();
    }

    /**
     * Returns the mutable list of Bugémons in this team.
     *
     * <p>
     * This method is intended for model and service logic. Views should usually
     * use {@link #getTeamDTO()} instead.
     * </p>
     *
     * @return the list of Bugémons
     */
    public List<Bugemon> getTeam() {
        return team;
    }

    /**
     * Returns the Bugémon currently active on the battlefield.
     *
     * @return the on-field Bugémon, or {@code null} if no Bugémon is currently on the field
     */
    @Override
    public Bugemon getOnFieldBugemon() {
        return onFieldBugemon;
    }

    /**
     * Returns the type of the Bugémon currently on the field.
     *
     * @return the type of the on-field Bugémon
     * @throws NullPointerException if no Bugémon is currently on the field
     */
    public String getOnFieldBugemonType() {
        return onFieldBugemon.getType();
    }

    /**
     * Returns the attacks of the Bugémon currently on the field.
     *
     * @return the list of attacks of the on-field Bugémon
     * @throws NullPointerException if no Bugémon is currently on the field
     */
    public List<Attack> getOnFieldBugemonAttacks() {
        return onFieldBugemon.getAttacks();
    }

    /**
     * Returns all attacks owned by all Bugémons in the team.
     *
     * @return a flat list containing every attack of every Bugémon
     */
    public List<Attack> getAllAttacks() {
        List<Attack> allAttacks = new ArrayList<>();

        for (Bugemon bug : team) {
            allAttacks.addAll(bug.getAttacks());
        }

        return allAttacks;
    }

    /**
     * Returns the attacks of a specific Bugémon identified by its id.
     *
     * @param bugemonId the id of the Bugémon
     * @return the list of attacks of the matching Bugémon
     * @throws BugemonNotInTeamException if no Bugémon with this id exists in the team
     */
    public List<Attack> getAttacksOf(String bugemonId) throws BugemonNotInTeamException {
        for (Bugemon bug : team) {
            if (bug.getId().equals(bugemonId)) {
                return bug.getAttacks();
            }
        }

        throw new BugemonNotInTeamException(bugemonId);
    }

    /**
     * Returns the Bugémons that are still able to fight.
     *
     * @return the list of Bugémons whose current HP is greater than zero
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
     * Changes the display name of the team.
     *
     * @param name the new display name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a Bugémon to the team.
     *
     * <p>
     * This method does not check the maximum team size. If the game requires a
     * limit, that rule should be enforced by the controller or service layer.
     * </p>
     *
     * @param bug the Bugémon to add
     */
    public void addBugemon(Bugemon bug) {
        team.add(bug);
    }

    /**
     * Adds a Bugémon to the team from a DTO reference.
     *
     * <p>
     * This method assumes that the DTO is actually a {@link Bugemon} instance.
     * It is mainly used by the selection view/controller logic.
     * </p>
     *
     * @param bug the Bugémon DTO to add
     * @throws ClassCastException if the given DTO is not a {@link Bugemon}
     */
    public void addBugemonDTO(BugemonDTO bug) {
        team.add((Bugemon) bug);
    }

    /**
     * Removes a Bugémon from the team using its id.
     *
     * @param bug the Bugémon DTO to remove
     */
    public void removeBugemonDTO(BugemonDTO bug) {
        team.removeIf(b -> b.getId().equals(bug.getId()));
    }

    /**
     * Changes the Bugémon currently active on the battlefield.
     *
     * <p>
     * The selected Bugémon must already belong to the team. When the Bugémon is
     * sent on the field, it is also marked as having participated in the fight.
     * </p>
     *
     * @param bug the Bugémon to send on the field
     * @throws BugemonNotInTeamException if the Bugémon does not belong to the team
     */
    public void changeOnFieldBugemon(Bugemon bug) throws BugemonNotInTeamException {
        for (Bugemon teamBugemon : team) {
            if (teamBugemon.getId().equals(bug.getId())) {
                onFieldBugemon = teamBugemon;
                onFieldBugemon.setHasParticipedToTrue();
                return;
            }
        }

        throw new BugemonNotInTeamException(bug.getId());
    }

    /**
     * Checks whether the current on-field Bugémon is KO.
     *
     * @return {@code true} if the on-field Bugémon is KO
     * @throws NullPointerException if no Bugémon is currently on the field
     */
    public boolean isOnFieldBugemonKO() {
        return onFieldBugemon.isKO();
    }

    /**
     * Checks whether at least one Bugémon in the team is still able to fight.
     *
     * @return {@code true} if at least one Bugémon is still standing
     */
    public boolean isTeamStillStanding() {
        return team.stream().anyMatch(Bugemon::isStillStanding);
    }

    /**
     * Checks whether one of the attacks in the team is {@code null}.
     *
     * <p>
     * This can be useful after loading a team from JSON to detect a badly
     * initialized attack.
     * </p>
     *
     * @return {@code true} if at least one attack is {@code null}
     */
    public boolean hasNullAttacks() {
        for (Attack attack : getAllAttacks()) {
            if (attack == null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Finds a Bugémon in the team by its id.
     *
     * @param id the id of the Bugémon to find
     * @return the matching Bugémon
     * @throws BugemonNotInTeamException if no Bugémon with this id exists in the team
     */
    public Bugemon getBugemonById(String id) throws BugemonNotInTeamException {
        for (Bugemon bug : this.getTeam()) {
            if (bug.getId().equals(id)) {
                return bug;
            }
        }

        throw new BugemonNotInTeamException(id);
    }
}