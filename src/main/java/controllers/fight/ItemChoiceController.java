package controllers.fight;

import controllers.Controller;
import controllers.MetaController;
import models.game.Item;
import views.fight.ItemsSubView;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class ItemChoiceController extends Controller<ItemsSubView> {

    private final Consumer<Item> onItemSelected;

    public ItemChoiceController(MetaController metaController,
                                List<Item> items,
                                Consumer<Item> onItemSelected) throws IOException {
        super(metaController, new ItemsSubView());
        this.onItemSelected = onItemSelected;
        this.view.setController(this);
        this.view.setItems(items);
    }

    public void onUseClicked() {
        Item selected = view.getSelectedItem();
        if (selected == null) {
            return;
        }
        onItemSelected.accept(selected);
        view.closeWindow();
    }

    public void onCloseClicked() {
        view.closeWindow();
    }

    public ItemsSubView getView() {
        return view;
    }
}