package views.listener;

/**
 * Listener interface for attack selection in the reward screen.
 * Decouples the view from the controller.
 */
public interface SelectAttackListener {

    /**
     * Called when the player selects an attack to replace.
     *
     * @param attackIndex index of the attack to replace (0-based)
     */
    void onAttackSelectedToReplace(int attackIndex);

    /**
     * Called when the player clicks the cancel button.
     */
    void onCancelClicked();
}