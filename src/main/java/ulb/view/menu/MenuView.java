package ulb.view.menu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class MenuView extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menuview.fxml"));
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        //String css = this.getClass().getResource("application.css").toExternalForm();
        //scene.getStylesheets().add(css);
        stage.setTitle("Bugémon");
        stage.setScene(scene);
        stage.show();
    }
}
