package controllers.fight;

import controllers.Controller;
import controllers.ControllerManager;
import models.game.Item;
import models.game.dto.ItemDTO;
import services.GameStateHolder;
import views.fight.ItemsView;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the item selection sub-view used during a fight.
 * <p>
 * This controller initializes the {@link ItemsView}, provides it with the list
 * of available items, and handles user actions coming from the sub-view.
 * It delegates the selected item through a callback and closes the sub-window
 * when the interaction is complete.
 * </p>
 */
public class ItemChoiceController extends Controller<ItemsView> {
    private final List<Item> items;
    /**
     * Creates the controller for the item selection sub-view.
     *
     * @param metaController the central controller responsible for application navigation
     * @throws IOException if the item selection view cannot be loaded
     */
    public ItemChoiceController(ControllerManager metaController) throws IOException {
        super(metaController, new ItemsView());
        this.items = GameStateHolder.getInstance().getAvailableItems();
        this.view.setController(this);
        this.view.setItems(items.stream().map(i -> (ItemDTO) i).toList());
    }

    /**
     * Handles the click on the "Use" button.
     * <p>
     * Retrieves the currently selected item from the view, invokes the selection
     * callback if an item is selected, and closes the sub-window.
     * </p>
     */
    public void onUseClicked() {
        ItemDTO selected = view.getSelectedItem();
        if (selected == null) return;
        for (Item item : items) {
            if (item.getId().equals(selected.getId())) {
                controllerManager.onItemChosen(item);
                view.closeWindow();
                return;
            }
        }
    }

    /**
     * Handles the click on the "Close" button by closing the sub-window.
     */
    public void onCloseClicked() {
        view.closeWindow();
    }

    /**
     * Returns the view managed by this controller.
     *
     * @return the item selection sub-view
     */
    public ItemsView getView() {
        return view;
    }


}