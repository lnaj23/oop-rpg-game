package services.game;

import models.bugemon.TeamBugemon;
import models.game.Item;

import java.util.List;

/**
 * Singleton holding the shared game session state.
 * Used to pass model objects between controllers without coupling them directly.
 */
public class GameStateHolder {

    private static GameStateHolder instance;

    private TeamBugemon currentTeamPlayer;

    private GameStateHolder() {}

    private final List<Item> availableItems = List.of(
            new Item("potion", "Une potion", "soigne 20 HP"),
            new Item("super_potion", "Une super potion", "soigne 50 HP"),
            new Item("revive", "La reanimation", "reanime un Bugemon KO")
    );

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
}