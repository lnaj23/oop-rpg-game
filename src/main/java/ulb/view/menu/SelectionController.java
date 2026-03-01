package ulb.view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ulb.models.Bugemon.Bugemon;
import ulb.models.Bugemon.BugemonProvider;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectionController implements Initializable {
    public Button btnReturn;
    public Label subtitleLabel;

    public void switchToMenuView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menuview.fxml"));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private ListView<Bugemon> myListView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myListView.getItems().addAll(BugemonProvider.getAllBugemons());

        myListView.setCellFactory(lv -> new ListCell<Bugemon>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(Bugemon bugemon, boolean empty) {
                super.updateItem(bugemon, empty);

                if (empty || bugemon == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String path = "/Assets/png/" + bugemon.getId() + ".png";

                    try {

                        Image img = new Image(getClass().getResourceAsStream(path));

                        imageView.setImage(img);
                        imageView.setFitHeight(40);
                        imageView.setPreserveRatio(true);

                        setText(bugemon.getNom());
                        setGraphic(imageView);

                    } catch (Exception e) {
                        setText(bugemon.getNom() + " (Image manquante)");
                        setGraphic(null);
                    }
                }
            }
        });
    }
}
