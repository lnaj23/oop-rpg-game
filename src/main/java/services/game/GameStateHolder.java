package services.game;

import models.bugemon.TeamBugemon;
import models.bugemon.dto.BugemonDTO;
import models.game.Item;
import services.dataprovider.TeamService;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton holding the shared game session state.
 * Used to pass model objects between controllers without coupling them directly.
 */
public class GameStateHolder {

    private static GameStateHolder instance;

    private TeamBugemon currentTeamPlayer;

    private final List<TeamBugemon> savedTeams = new ArrayList<>(TeamService.loadTeams());

    private GameStateHolder() {}

    private List<BugemonDTO> currentPlayerTeamDTO;
    private String currentOnFieldId;
    private boolean switchOnPurpose;

    private final List<Item> availableItems = new ArrayList<>(List.of(
            new Item("baie_revigorante", "Baie Revigorante", "soigne 20 HP"),
            new Item("baie_revigorante", "Baie Revigorante", "soigne 20 HP"),
            new Item("baie_revigorante", "Baie Revigorante", "soigne 20 HP"),
            new Item("baie_tonique", "Baie Tonique", "soigne 50 HP"),
            new Item("baie_tonique", "Baie Tonique", "soigne 50 HP"),
            new Item("gel_defensif", "Gel Défensif", "augmente la défense"),
            new Item("serum_offensif", "Sérum Offensif", "augmente l'attaque")
    ));

    /**
     * Returns the unique instance of {@link GameStateHolder}.
     *
     * @return the singleton instance
     */
    public static GameStateHolder getInstance() {
        if (instance == null) {
            instance = new GameStateHolder();
        }
        return instance;
    }

    public List<TeamBugemon> getSavedTeams() {
        return savedTeams;
    }
    /**
     * Sets the player's current team for the upcoming fight.
     *
     * @param team the team selected by the player
     */
    public void setCurrentTeamPlayer(TeamBugemon team) {
        this.currentTeamPlayer = team;
    }

    /**
     * Returns the player's current team.
     *
     * @return the current player team
     */
    public TeamBugemon getCurrentTeamPlayer() {
        return currentTeamPlayer;
    }


    /**
     * Clears the session state.
     * Should be called when returning to the title screen.
     */
    public void clearCurrentTeamPlayer() {
        this.currentTeamPlayer = null;
    }

    public List<Item> getAvailableItems() {
        return availableItems;
    }

    public void consumeItem(Item items) {
        this.availableItems.remove(items);
    }

    public void gainItem(Item items) {
        this.availableItems.add(items);
    }

    public void setCurrentPlayerTeamDTO(List<BugemonDTO> team) { this.currentPlayerTeamDTO = team; }
    public List<BugemonDTO> getCurrentPlayerTeamDTO() { return currentPlayerTeamDTO; }

    public void setCurrentOnFieldId(String id) { this.currentOnFieldId = id; }
    public String getCurrentOnFieldId() { return currentOnFieldId; }

    public void setSwitchOnPurpose(boolean onPurpose) { this.switchOnPurpose = onPurpose; }
    public boolean isSwitchOnPurpose() { return switchOnPurpose; }
}