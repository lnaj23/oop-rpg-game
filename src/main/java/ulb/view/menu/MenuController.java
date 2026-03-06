package ulb.view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import java.io.IOException;

public class MenuController {
    @FXML
    public void switchToSelectionView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("selectionview.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root); // Remplace le contenu sans changer la scène
    }

    @FXML
    private void switchToPreBuildTeamPage(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("prebuildteam.fxml"));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}