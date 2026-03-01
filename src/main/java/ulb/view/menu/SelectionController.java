package ulb.view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ulb.models.Bugemon.Bugemon;
import ulb.models.Bugemon.BugemonProvider;
import ulb.models.Bugemon.TeamBugemon;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectionController implements Initializable {

    public void switchToMenuView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menuview.fxml"));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private GridPane gridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayBugemons();
    }

    private void displayBugemons() {

        List<Bugemon> allBugemons = BugemonProvider.getAllBugemons();

        List<Node> buttons = gridPane.getChildren().filtered(node -> node instanceof Button);

        for (int i = 0; i < allBugemons.size() && i < buttons.size(); i++) {
            Bugemon bugemon = allBugemons.get(i);
            Button btn = (Button) buttons.get(i);
            btn.setText(bugemon.getNom());

            try {
                String path = "/Assets/png/" + bugemon.getSprite();
                Image img = new Image(getClass().getResourceAsStream(path));
                ImageView view = new ImageView(img);
                view.setFitHeight(50);
                view.setPreserveRatio(true);
                btn.setGraphic(view);
                btn.setContentDisplay(javafx.scene.control.ContentDisplay.TOP);
            }
            catch (Exception e) {
                System.err.println("Impossible de charger l'image pour : " + bugemon.getNom());
            }
        }
    }
}
