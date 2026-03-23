package views.fight;

import controllers.fight.ItemChoiceController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import models.bugemon.dto.ItemDTO;
import views.View;

import java.io.IOException;
import java.util.List;
/**
 * View for the item selection sub-window displayed during a fight.
 * <p>
 * This view displays the list of available items and forwards user actions
 * to its associated {@link ItemChoiceController}.
 * </p>
 */
public class ItemsView extends View<ItemChoiceController> {

    @FXML private ListView<ItemDTO> itemListView;
    @FXML private Button btnUse;
    @FXML private Button btnClose;
    /**
     * Creates the item selection sub-view and loads its FXML layout.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public ItemsView() throws IOException {
        super("/fxml/fight/itemsview.fxml");
    }

    /**
     * Initializes the view after FXML loading.
     * <p>
     * Binds button actions to the controller and updates the state of the
     * "Use" button depending on the current selection.
     * </p>
     */
    @FXML
    public void initialize() {
        btnUse.setOnAction(e -> controller.onUseClicked());
        btnClose.setOnAction(e -> controller.onCloseClicked());

        itemListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> btnUse.setDisable(newVal == null)
        );
    }
    /**
     * Returns the title of the item selection sub-window.
     *
     * @return the window title
     */
    @Override
    protected String getTitle() {
        return "Sac d'objets";
    }

    /**
     * Populates the list view with the given items.
     *
     * @param items the items to display
     */
    public void setItems(List<ItemDTO> items) {
        itemListView.setItems(FXCollections.observableArrayList(items));
    }

    /**
     * Returns the currently selected item in the list.
     *
     * @return the selected item, or {@code null} if none is selected
     */
    public ItemDTO getSelectedItem() {
        return itemListView.getSelectionModel().getSelectedItem();
    }
}