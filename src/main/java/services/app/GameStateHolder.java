package services.app;

import exceptions.JsonResourceNotFoundException;
import exceptions.TowerNotInitializedException;
import models.bugemon.TeamBugemon;
import models.game.Item;
import models.game.LevelTower;
import models.game.TowerNodeType;
import services.controller.dataprovider.ItemProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Singleton responsible for holding the temporary state of the current game session.
 *
 * <p>
 * This class is used to share the current game state between several controllers
 * without forcing those controllers to directly depend on each other.
 * </p>
 *
 * <p>
 * It should only store runtime information, such as:
 * </p>
 *
 * <ul>
 *     <li>the current tower;</li>
 *     <li>the current player team used during the game;</li>
 *     <li>the id of the saved team currently being played, if there is one;</li>
 *     <li>the current inventory available during the run.</li>
 * </ul>
 *
 * <p>
 * This class should not store all saved teams. Saved teams belong to the
 * persistence layer and must be loaded or saved through the team provider /
 * repository when needed. This keeps the JSON file as the source of truth.
 * </p>
 */
public class GameStateHolder {

    /**
     * Unique instance of the singleton.
     */
    private static GameStateHolder instance;

    /**
     * Provider used to load the default inventory items.
     */
    private static final ItemProvider itemProvider = new ItemProvider();

    /**
     * Items currently available during the game session.
     *
     * <p>
     * This list represents the player's current inventory during the run.
     * It is initialized from the default items when the singleton is created.
     * </p>
     */
    private static List<Item> availableItems = new ArrayList<>();

    /**
     * Current tower progression.
     *
     * <p>
     * This contains the current level, current step, and tower progression.
     * It is reset when the game session ends.
     * </p>
     */
    private LevelTower currentLevelTower;

    /**
     * Current player team used during the active game session.
     *
     * <p>
     * This can be either:
     * </p>
     *
     * <ul>
     *     <li>a team loaded from the saved teams JSON file;</li>
     *     <li>a temporary team created only for the current run.</li>
     * </ul>
     */
    private TeamBugemon currentTeamPlayer;

    /**
     * Id of the saved team currently being played.
     *
     * <p>
     * If this value is not {@code null}, it means that the current team comes
     * from the saved teams JSON file. This id can then be used to save the
     * updated state of the team after a game.
     * </p>
     *
     * <p>
     * If this value is {@code null}, it means that the current team is temporary
     * and should not automatically overwrite a saved team.
     * </p>
     */
    private String currentSavedTeamId;

    /**
     * Creates the shared game state holder.
     *
     * <p>
     * The constructor is private because this class follows the singleton
     * pattern. Controllers must use {@link #getInstance()} instead of creating
     * a new instance directly.
     * </p>
     *
     * <p>
     * The constructor initializes the default inventory. If the default items
     * cannot be loaded, the inventory is initialized as an empty list so the
     * application can still start.
     * </p>
     */
    private GameStateHolder() {
        try {
            availableItems = new ArrayList<>(itemProvider.getDefautItems());
            availableItems.sort(Comparator.comparing(Item::getName));
        } catch (JsonResourceNotFoundException e) {
            availableItems = new ArrayList<>();
        }
    }

    /**
     * Returns the unique instance of the game state holder.
     *
     * <p>
     * The instance is created lazily the first time this method is called.
     * </p>
     *
     * @return the unique {@code GameStateHolder} instance
     */
    public static GameStateHolder getInstance() {
        if (instance == null) {
            instance = new GameStateHolder();
        }

        return instance;
    }

    /**
     * Returns the type of the current tower step.
     *
     * @return the current tower step type
     * @throws TowerNotInitializedException if no tower is currently initialized
     */
    public TowerNodeType getCurrentStepType() throws TowerNotInitializedException {
        if (currentLevelTower == null) {
            throw new TowerNotInitializedException();
        }

        return currentLevelTower.getCurrentStepType();
    }

    /**
     * Sets the current level tower.
     *
     * <p>
     * This is usually called when a new tower run starts.
     * </p>
     *
     * @param levelTower the tower to use for the current game session
     */
    public void setCurrentLevelTower(LevelTower levelTower) {
        this.currentLevelTower = levelTower;
    }

    /**
     * Returns the current level tower.
     *
     * @return the current tower, or {@code null} if no tower is active
     */
    public LevelTower getCurrentLevelTower() {
        return currentLevelTower;
    }

    /**
     * Advances the current tower to the next step.
     *
     * <p>
     * This method assumes that a tower is already initialized.
     * </p>
     *
     * @throws NullPointerException if no tower is currently initialized
     */
    public void advanceStep() {
        this.currentLevelTower.advance();
    }

    /**
     * Clears the current tower.
     *
     * <p>
     * This is typically called when the player returns to the main menu or when
     * the current game session ends.
     * </p>
     */
    public void clearCurrentLevelTower() {
        this.currentLevelTower = null;
    }

    /**
     * Starts a game session with a saved team.
     *
     * <p>
     * The team is stored as the current player team, and its saved team id is
     * stored separately. This allows the application to update the correct team
     * in the JSON file after the game without relying on the team's index in a
     * list.
     * </p>
     *
     * @param team   the team used for the current game session
     * @param teamId the stable id of the saved team in the JSON file
     */
    public void startSavedTeam(TeamBugemon team, String teamId) {
        this.currentTeamPlayer = team;
        this.currentSavedTeamId = teamId;
    }

    /**
     * Starts a game session with a temporary unsaved team.
     *
     * <p>
     * Since the team does not come from the saved teams JSON file, no saved team
     * id is stored. This prevents the temporary team from accidentally replacing
     * an existing saved team.
     * </p>
     *
     * @param team the temporary team used for the current game session
     */
    public void startUnsavedTeam(TeamBugemon team) {
        this.currentTeamPlayer = team;
        this.currentSavedTeamId = null;
    }

    /**
     * Updates the current player team.
     *
     * <p>
     * This method only replaces the team object. It does not clear the saved
     * team id. This is important during a game: after a fight, the updated team
     * may replace the previous team object, but it must still be linked to the
     * same saved team if the run started from a saved team.
     * </p>
     *
     * @param team the new current player team
     */
    public void setCurrentTeamPlayer(TeamBugemon team) {
        this.currentTeamPlayer = team;
    }

    /**
     * Returns the current player team.
     *
     * @return the current player team, or {@code null} if no game session is active
     */
    public TeamBugemon getCurrentTeamPlayer() {
        return currentTeamPlayer;
    }

    /**
     * Returns the id of the saved team currently being played.
     *
     * <p>
     * This id is used to update the correct team in the JSON file after a game.
     * </p>
     *
     * @return the saved team id, or {@code null} if the current team is temporary
     */
    public String getCurrentSavedTeamId() {
        return currentSavedTeamId;
    }

    /**
     * Clears the current player team and its saved team id.
     *
     * <p>
     * This should be called when the game session ends or when the player
     * returns to the main menu.
     * </p>
     */
    public void clearCurrentTeamPlayer() {
        this.currentTeamPlayer = null;
        this.currentSavedTeamId = null;
    }

    /**
     * Returns the current available items.
     *
     * <p>
     * The returned list is the actual inventory list used by the current game
     * session.
     * </p>
     *
     * @return the current list of available items
     */
    public List<Item> getAvailableItems() {
        return availableItems;
    }

    /**
     * Removes an item from the current inventory.
     *
     * <p>
     * This method removes one occurrence of the given item from the available
     * items list.
     * </p>
     *
     * @param item the item to consume
     */
    public void consumeItem(Item item) {
        availableItems.remove(item);
    }

    /**
     * Adds an item to the current inventory.
     *
     * <p>
     * After the item is added, the inventory is sorted by item name to keep a
     * stable display order in the interface.
     * </p>
     *
     * @param item the item to add
     */
    public void gainItem(Item item) {
        availableItems.add(item);
        availableItems.sort(Comparator.comparing(Item::getName));
    }

    public void resetItems() throws JsonResourceNotFoundException {
        availableItems = new ArrayList<>(itemProvider.getDefautItems());
        availableItems.sort(Comparator.comparing(Item::getName));
    }
}