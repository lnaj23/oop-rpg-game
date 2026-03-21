package views.fight;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.game.Item;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * View for the item selection screen.
 * Displays a list of items and handles the selection through a callback.
 */
public class ItemsSubView {

    @FXML private ListView<Item> itemListView;
    @FXML private Button btnUse;
    @FXML private Button btnClose;

    private final Stage stage;

    /**
     * Constructs the items view.
     * * @param items The list of available items to display.
     * @param onItemSelected Callback triggered when an item is used.
     */
    public ItemsSubView(List<Item> items, Consumer<Item> onItemSelected) {
        this.stage = new Stage();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/fight/itemsview.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource("/css/common.css")).toExternalForm()
            );
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);

            itemListView.setItems(FXCollections.observableArrayList(items));
            itemListView.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> btnUse.setDisable(newVal == null)
            );
            btnUse.setOnAction(e -> {
                Item selected = itemListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    onItemSelected.accept(selected);
                    stage.close();
                }
            });

            btnClose.setOnAction(e -> stage.close());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the modal window and waits for user interaction.
     */
    public void show() {
        stage.showAndWait();
    }
}