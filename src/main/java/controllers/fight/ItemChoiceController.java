package controllers.fight;

import controllers.Controller;
import controllers.MetaController;
import models.game.Item;
import views.fight.ItemsSubView;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
/**
 * Controller for the item selection sub-view used during a fight.
 * <p>
 * This controller initializes the {@link ItemsSubView}, provides it with the list
 * of available items, and handles user actions coming from the sub-view.
 * It delegates the selected item through a callback and closes the sub-window
 * when the interaction is complete.
 * </p>
 */
public class ItemChoiceController extends Controller<ItemsSubView> {
    private final Consumer<Item> onItemSelected;

    /**
     * Creates the controller for the item selection sub-view.
     *
     * @param metaController the central controller responsible for application navigation
     * @param items the list of items to display in the sub-view
     * @param onItemSelected callback executed when the user selects an item
     * @throws IOException if the item selection view cannot be loaded
     */
    public ItemChoiceController(MetaController metaController,
                                List<Item> items,
                                Consumer<Item> onItemSelected) throws IOException {
        super(metaController, new ItemsSubView());
        this.onItemSelected = onItemSelected;
        this.view.setController(this);
        this.view.setItems(items);
    }

    /**
     * Handles the click on the "Use" button.
     * <p>
     * Retrieves the currently selected item from the view, invokes the selection
     * callback if an item is selected, and closes the sub-window.
     * </p>
     */
    public void onUseClicked() {
        Item selected = view.getSelectedItem();
        if (selected == null) {
            return;
        }
        onItemSelected.accept(selected);
        view.closeWindow();
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
    public ItemsSubView getView() {
        return view;
    }
}