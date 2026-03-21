package controllers.fight;

import controllers.Controller;
import controllers.MetaController;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import models.bugemon.Bugemon;
import models.game.Fight;
import models.game.Item;
import listener.FightListener;
import views.fight.FightView;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import models.bugemon.TeamBugemon;

/**
 * Controller for the fight screen.
 * <p>
 * Orchestrates the turn-based combat loop between the player and the enemy:
 * handles attack selection, enemy AI reaction, on-field Bugémon switches,
 * and delegates all visual updates to {@link FightView}.
 * </p>
 * <p>
 * Implements {@link FightListener} so that {@link FightView} can forward user
 * actions without holding a direct reference to this class.
 * </p>
 */
public class FightController extends Controller<FightView> implements FightListener {

    /** The current fight instance holding all battle state. */
    private Fight fight;

    /** Random number generator used to pick enemy and switch targets. */
    private final Random random = new Random();

    /** Delay in seconds before the enemy reacts after the player's attack. */
    private static final int ENEMY_REACT_TIME = 2;

    /** Delay in seconds before switching out a fainted Bugémon. */
    private static final double DISPLAY_PLAYER_DEATH_NOTE_TIME = 1.5;

    /**
     * Constructs the controller, initialises the associated {@link FightView},
     * and registers this instance as the view's {@link FightListener}.
     *
     * @param metaController the meta-controller used to switch scenes
     * @throws IOException if the FXML file for the view cannot be loaded
     */
    public FightController(MetaController metaController) throws IOException {
        super(metaController, new FightView());
        this.view.setListener(this);
    }

    /**
     * Initialises the fight with the given player team and a hardcoded enemy team,
     * then updates the view to reflect the initial battle state.
     *
     * @param team the player's team
     */
    public void setPlayerTeam(TeamBugemon team) {
        view.resetDisplay();
        fight = new Fight(team, new TeamBugemon(List.of("pyricore", "moussil", "pebblit")));
        view.displayTeam(fight.getTeamPlayer().getTeamDTO());
        view.displayTeam(fight.getTeamEnemy().getTeamDTO());
        fight.computePredictedEffectiveness();
        view.updatePredictedEffectiveness(fight.getPredictedEffectiveness());
        view.updateActiveBugemonSprite(fight.getOnFieldPlayerBugemon());
        view.updateActiveBugemonSprite(fight.getOnFieldEnemyBugemon());
        String startFightMessage = "L'adversaire envoie " + fight.getOnFieldEnemyBugemonName() + " ! "
                + (!fight.playerCanStart()
                ? "L'adversaire a plus d'initiative, il lance les hostilités !"
                : "L'adversaire a moins d'initiative, vous lancez les hostilités !");
        view.updateCombatMessage(startFightMessage);
        if (!fight.playerCanStart()) {
            forceEnemyToAttack();
        }
    }

    /**
     * Navigates back to the title screen.
     */
    public void goToTitleScreen() {
        this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN);
    }

    /**
     * Called when the player selects an attack.
     * Triggers the player's attack, then schedules the enemy's counterattack
     * after {@link #ENEMY_REACT_TIME} seconds. Checks if the battle is over
     * after the player's attack.
     *
     * @param attackIndex the index of the selected attack in the player's attack list
     */
    @Override
    public void onAttackSelected(int attackIndex) {
        if (!fight.isPlayerTurn()) return;

        view.setButtonsDisabled(true);
        view.showAttackandActionPanels(false);

        playerAttackEnemy(attackIndex);
    }

    /**
     * Called when the player clicks the item button.
     * Opens a window with a list of hardcoded items.
     */
    @Override
    public void onItemClicked() {
        List<Item> hardcodedItems = List.of(
                new Item("potion", "Une potion", "soigne 20 HP"),
                new Item("super_potion", "Une super potion", "soigne 50 HP"),
                new Item("revive", "La reanimation", "reanime un Bugemon KO")
        );

        try {
            controllers.fight.ItemChoiceController itemChoiceController =
                    new controllers.fight.ItemChoiceController(metaController, hardcodedItems, this::useItem);
            metaController.openSubScene(itemChoiceController.getView(), true);
        } catch (IOException e) {
            metaController.fatalError("Impossible d'ouvrir la vue des objets : " + e.getMessage());
        }
    }

    /**
     * Called when the player clicks the switch button.
     * Opens the switch window; the enemy attacks afterwards.
     */
    @Override
    public void onSwitchDisplaeyPlayerClicked() {
        view.setButtonsDisabled(true);
        showSwitchWindow(true);
    }

    /**
     * Called when the player clicks the forfeit button.
     * Immediately navigates back to the title screen.
     */
    @Override
    public void onForfeitClicked() {
        this.view.showMessageReturnTitle(
                "Abandon",
                "Vous avez abandonné le combat.",
                () -> this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN)
        );
    }

    /**
     * Uses the selected item, displays a combat message, and ends the player's turn.
     *
     * @param item the item to be used
     */
    private void useItem(Item item) {
        view.setButtonsDisabled(true);
        view.updateCombatMessage("Tu utilises " + item.getName() + " !");
        // TODO : appliquer l'effet de l'item sur le modèle fight
        forceEnemyToAttack();
    }

    /**
     * Executes the player's attack against the enemy and updates the view.
     * If the enemy's on-field Bugémon is KO after the attack, switches it out.
     * Checks if the battle is over after each step.
     *
     * @param attackIndex the index of the attack to use
     */
    private void playerAttackEnemy(int attackIndex) {
        fight.playerAttack(attackIndex);
        viewReactToAttack(fight.getOnFieldEnemyBugemon());

        if (fight.isOnFieldEnemyBugemonKO()) {
            view.markBugemonAsFainted(fight.getOnFieldEnemyBugemon());
            PauseTransition pauseKO = new PauseTransition(Duration.seconds(DISPLAY_PLAYER_DEATH_NOTE_TIME));
            pauseKO.setOnFinished(e -> {
                if (fight.isBattleOver()) {
                    endIfBattleOver();
                } else {
                    switchDisplayEnemyBugemon();
                    enemyAttackPlayer();
                }
            });
            pauseKO.play();
        } else {
            if (fight.isBattleOver()) endIfBattleOver();
            else enemyAttackPlayer();
        }
    }

    /**
     * Schedules and executes the enemy's attack against the player after a delay
     * of {@link #ENEMY_REACT_TIME} seconds.
     * If the player's on-field Bugémon is KO after the attack, marks it as fainted
     * and opens the switch window. Re-enables buttons once the attack is resolved.
     */
    private void enemyAttackPlayer() {
        PauseTransition pause = new PauseTransition(Duration.seconds(ENEMY_REACT_TIME));
        pause.setOnFinished(e -> {
            Bugemon target = fight.getOnFieldPlayerBugemon();
            fight.enemyAttack();
            viewReactToAttack(target);

            if (fight.isOnFieldPlayerBugemonKO()) {
                view.markBugemonAsFainted(target);
                PauseTransition pauseKO = new PauseTransition(Duration.seconds(DISPLAY_PLAYER_DEATH_NOTE_TIME));
                pauseKO.setOnFinished(e2 -> {
                    if (fight.isBattleOver()) {
                        endIfBattleOver();
                    } else {
                        Platform.runLater(() -> showSwitchWindow(false));
                    }
                });
                pauseKO.play();
            } else {
                view.setButtonsDisabled(false);
            }
        });
        pause.play();
    }

    /**
     * Switches the enemy's on-field Bugémon to a random available one
     * and updates the view accordingly.
     */
    private void switchDisplayEnemyBugemon() {
        List<Bugemon> available = fight.getAvailableEnemyBugemons();
        if (available.isEmpty()) return;
        Bugemon randomBug = available.get(random.nextInt(available.size()));
        fight.changeEnemyOnField(randomBug);
        view.updateActiveBugemonSprite(fight.getOnFieldEnemyBugemon());
        view.updateCombatMessage("L'adversaire envoie " + randomBug.getName() + " !");
    }

    /**
     * Forces the enemy to take a turn immediately, disabling player buttons in the meantime.
     * {@link #endIfBattleOver()} is handled inside {@link #enemyAttackPlayer()} callbacks,
     * so it must NOT be called here to avoid premature battle-end detection.
     */
    private void forceEnemyToAttack() {
        view.setButtonsDisabled(true);
        fight.forceEnemyTurn();
        enemyAttackPlayer();
    }

    /**
     * Updates the HP display and combat message in the view after an attack lands
     * on the given Bugémon.
     *
     * @param bug the Bugémon that received the attack
     */
    private void viewReactToAttack(Bugemon bug) {
        view.updateBugemonHPDisplay(bug);
        view.updateCombatMessage(fight.getActionResultText());
    }

    /**
     * Displays the battle outcome (victory or defeat) then navigates to the title screen.
     * Does nothing if the battle is not yet over.
     */
    private void endIfBattleOver() {
        if (!fight.isBattleOver()) return;

        view.setButtonsDisabled(true);

        if (fight.playerWon()) {
            view.showMessageReturnTitle(
                    "Victoire",
                    "Vous avez gagné le combat !",
                    this::goToTitleScreen
            );
        } else {
            view.showMessageReturnTitle(
                    "Défaite",
                    "Vous avez perdu le combat.",
                    this::goToTitleScreen
            );
        }
    }

    /**
     * Switches the player's on-field Bugémon to the selected one and updates the view.
     *
     * @param selectedBugemon the Bugémon to switch in
     */
    private void switchPlayerBugemon(Bugemon selectedBugemon) {
        String switchMessage = fight.getOnFieldPlayerBugemonName() + ", reviens !\n"
                + selectedBugemon.getName() + " entre en jeu !";
        fight.changePlayerOnField(selectedBugemon);
        fight.computePredictedEffectiveness();
        view.updatePredictedEffectiveness(fight.getPredictedEffectiveness());
        view.updateActiveBugemonSprite(fight.getOnFieldPlayerBugemon());
        view.updateCombatMessage(switchMessage);
    }

    /**
     * Finds a Bugémon in the available player Bugémons by its ID.
     *
     * @param id the ID to look for
     * @return the matching Bugémon
     * @throws IllegalArgumentException if no Bugémon with that ID is found
     */
    private Bugemon findAvailablePlayerBugemonById(Object id) {
        for (Bugemon bugemon : fight.getAvailablePlayerBugemons()) {
            if (Objects.equals(bugemon.getId(), id)) {
                return bugemon;
            }
        }
        throw new IllegalArgumentException("Bugémon introuvable : " + id);
    }

    /**
     * Opens the switch window for the player.
     *
     * @param onPurpose {@code true} if the player chose to switch voluntarily
     *                  (costs a turn), {@code false} if forced after a KO
     */
    private void showSwitchWindow(boolean onPurpose) {
        try {
            controllers.fight.SwitchBugemonController switchController = new controllers.fight.SwitchBugemonController(
                    metaController,
                    fight.getTeamPlayer().getTeamDTO(),
                    onPurpose,
                    fight.getOnFieldPlayerBugemonId(),
                    selectedId -> {
                        Bugemon selectedBugemon = findAvailablePlayerBugemonById(selectedId);
                        switchPlayerBugemon(selectedBugemon);
                        if (onPurpose) {
                            forceEnemyToAttack();
                        } else {
                            view.setButtonsDisabled(false);
                        }
                    },
                    () -> view.setButtonsDisabled(false)
            );

            metaController.openSubScene(switchController.getView(), !onPurpose);
        } catch (IOException e) {
            metaController.fatalError("Impossible d'ouvrir la vue de changement : " + e.getMessage());
        }
    }
}