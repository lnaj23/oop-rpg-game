package views.fight;

import configuration.AppConfig;
import controllers.fight.ItemChoiceController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import models.game.dto.ItemDTO;
import views.View;

import java.io.IOException;
import java.util.List;

public class ItemsView extends View<ItemChoiceController> {

    @FXML private ListView<ItemDTO> itemListView;
    @FXML private Button btnUse;
    @FXML private Button btnClose;

    public ItemsView() throws IOException {
        super(AppConfig.FXML_ITEMS);
    }

    @FXML
    public void initialize() {
        btnUse.setOnAction(e -> controller.onUseClicked());
        btnClose.setOnAction(e -> controller.onCloseClicked());

        itemListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(ItemDTO item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " : " + item.getDescription());
                }
            }
        });

        itemListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> btnUse.setDisable(newVal == null)
        );
    }

    @Override
    protected String getTitle() {
        return "Sac d'objets";
    }

    public void setItems(List<ItemDTO> items) {
        itemListView.setItems(FXCollections.observableArrayList(items));
    }

    public ItemDTO getSelectedItem() {
        return itemListView.getSelectionModel().getSelectedItem();
    }
}