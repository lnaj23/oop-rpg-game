package services.game;

import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import exceptions.LoadingTeamFailedException;
import exceptions.TowerNotInitializedException;
import models.TowerNodeType;
import configuration.AppConfig;
import models.TowerNodeType;
import models.bugemon.TeamBugemon;
import models.bugemon.dto.BugemonDTO;
import models.game.Item;
import repositories.ItemRepository;
import services.dataprovider.ItemProvider;
import models.game.LevelTower;
import services.dataprovider.ItemProvider;
import services.dataprovider.TeamProvider;
import models.game.LevelTower;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton holding the shared game session state.
 * Used to pass model objects between controllers without coupling them directly.
 */
public class GameStateHolder {
    private LevelTower currentLevelTower;

    private static GameStateHolder instance;

    private TeamBugemon currentTeamPlayer;

    private TeamProvider teamProvider;
    private List<TeamBugemon> savedTeams;

    private GameStateHolder(){
        List<TeamBugemon> loaded;
        try {
            teamProvider = new TeamProvider();
            availableItems = itemProvider.getDefautItems();
            loaded = new ArrayList<>(teamProvider.loadTeams());
        } catch (LoadingTeamFailedException | AttackNotFoundException | JsonResourceNotFoundException e) {
            loaded = new ArrayList<>();
        }
        this.savedTeams = loaded;
    }

    private List<BugemonDTO> currentPlayerTeamDTO;
    private String currentOnFieldId;
    private boolean switchOnPurpose;

    private static List<Item> availableItems =  new ArrayList<>();
    private static ItemProvider itemProvider = new ItemProvider();
    /**
     * Returns the unique instance of {@link GameStateHolder}.
     *
     * @return the singleton instance
     */
    public static GameStateHolder getInstance(){
        if (instance == null) {
            instance = new GameStateHolder();
        }
        return instance;
    }
    public TowerNodeType getCurrentStepType() throws TowerNotInitializedException {
        if (currentLevelTower == null) {
            throw new TowerNotInitializedException();
        }
        return currentLevelTower.getCurrentStepType();
    }
    /**
     * Sets the current level tower for the game session.
     * <p>
     * Stores the tower instance so it can be accessed across controllers
     * during tower progression.
     * </p>
     *
     * @param levelTower the {@link LevelTower} instance to set
     */
    public void setCurrentLevelTower(LevelTower levelTower) {
        this.currentLevelTower = levelTower;
    }

    /**
     * Returns the current level tower.
     *
     * @return the active {@link LevelTower} instance, or {@code null} if none is set
     */
    public LevelTower getCurrentLevelTower() {
        return currentLevelTower;
    }
    /**
     * Advances the current level tower to the next step.
     * <p>
     * Delegates to the {@link LevelTower#advance()} method to progress the player
     * through the tower's floors and steps. Should be called after each tower node
     * is completed.
     * </p>
     *
     * @throws NullPointerException if the current level tower is not set
     */
    public void advanceStep(){
        this.currentLevelTower.advance();
    }

    /**
     * Clears the current level tower from the session state.
     * <p>
     * Should be called when exiting the tower or returning to the title screen.
     * </p>
     */
    public void clearCurrentLevelTower() {
        this.currentLevelTower = null;
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