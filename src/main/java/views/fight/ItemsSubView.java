package views.fight;

import controllers.fight.ItemChoiceController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import models.game.Item;
import views.View;

import java.io.IOException;
import java.util.List;

/**
 * View for the item selection screen.
 * Displays a list of items and handles the selection through a callback.
 */
public class ItemsSubView extends View<ItemChoiceController> {

    @FXML private ListView<Item> itemListView;
    @FXML private Button btnUse;
    @FXML private Button btnClose;

    public ItemsSubView() throws IOException {
        super("/fxml/fight/itemsview.fxml");
    }

    @FXML
    public void initialize() {
        btnUse.setOnAction(e -> controller.onUseClicked());
        btnClose.setOnAction(e -> controller.onCloseClicked());

        itemListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> btnUse.setDisable(newVal == null)
        );
    }

    @Override
    protected String getTitle() {
        return "Sac d'objets";
    }

    public void setItems(List<Item> items) {
        itemListView.setItems(FXCollections.observableArrayList(items));
    }

    public Item getSelectedItem() {
        return itemListView.getSelectionModel().getSelectedItem();
    }
}