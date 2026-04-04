package listener;


import controllers.fight.FightAgainstCPUController;

/**
 * Listener interface decoupling {@link views.fight.FightView} from any concrete controller.
 * <p>
 * {@link FightAgainstCPUController} implements this interface so that
 * {@code FightView} never needs to import or reference the controller class directly.
 * </p>
 */
public interface FightListener {
    /**
     * Called when the player selects an attack.
     *
     * @param attackIndex index of the selected attack (0-based)
     */
    void onAttackSelected(int attackIndex);

    /**
     * Called when the player clicks the item button.
     */
    void onItemClicked();

    /**
     * Called when the player clicks the switch button.
     */
    void onSwitchDisplaeyPlayerClicked();

    /**
     * Called when the player clicks the forfeit button.
     */
    void onForfeitClicked();
}
