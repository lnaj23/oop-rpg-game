package services.app;

import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import exceptions.LoadingTeamFailedException;
import exceptions.TowerNotInitializedException;
import models.game.TowerNodeType;
import models.bugemon.TeamBugemon;
import models.game.Item;
import services.controller.dataprovider.ItemProvider;
import models.game.LevelTower;
import services.controller.dataprovider.TeamProvider;

import java.util.ArrayList;
import java.util.Comparator;
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

    private GameStateHolder() {
        List<TeamBugemon> loaded;
        try {
            teamProvider = new TeamProvider();
            availableItems = new ArrayList<>(itemProvider.getDefautItems());
            availableItems.sort(Comparator.comparing(Item::getName));
            loaded = new ArrayList<>(teamProvider.loadTeams());
        } catch (LoadingTeamFailedException | AttackNotFoundException | JsonResourceNotFoundException e) {
            loaded = new ArrayList<>();
        }
        this.savedTeams = loaded;
    }

    private static List<Item> availableItems = new ArrayList<>();
    private static ItemProvider itemProvider = new ItemProvider();

    public static GameStateHolder getInstance() {
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

    public void setCurrentLevelTower(LevelTower levelTower) {
        this.currentLevelTower = levelTower;
    }

    public LevelTower getCurrentLevelTower() {
        return currentLevelTower;
    }

    public void advanceStep() {
        this.currentLevelTower.advance();
    }

    public void clearCurrentLevelTower() {
        this.currentLevelTower = null;
    }

    public List<TeamBugemon> getSavedTeams() {
        return savedTeams;
    }

    public void setCurrentTeamPlayer(TeamBugemon team) {
        this.currentTeamPlayer = team;
    }

    public TeamBugemon getCurrentTeamPlayer() {
        return currentTeamPlayer;
    }

    public void clearCurrentTeamPlayer() {
        this.currentTeamPlayer = null;
    }

    public List<Item> getAvailableItems() {
        return availableItems;
    }

    public void consumeItem(Item items) {
        this.availableItems.remove(items);
    }

    public void gainItem(Item item) {
        this.availableItems.add(item);
        this.availableItems.sort(Comparator.comparing(Item::getName));
    }
}