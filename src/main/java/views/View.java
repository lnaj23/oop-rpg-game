package views;

import java.io.IOException;
import java.net.URL;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public abstract class View {

    protected final Pane root;
    protected final Scene scene;

    public View(String fxmlPath) throws IOException {
        URL url = View.class.getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController((Object) this);
        this.root = loader.load();
        this.scene = new Scene(root);
    }

    public void show(Stage stage) {
        stage.setScene(this.scene);
        stage.setTitle(this.getTitle());
        if (!stage.isShowing()) {
            stage.show();
        }
    }

    protected abstract String getTitle();
}