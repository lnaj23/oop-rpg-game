package views;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import controllers.Controller;
import javafx.stage.StageStyle;
import models.Bugemon.DTO.TeamBugemonDTO;

/**
 * Abstract base class for all views.
 * Loads an FXML file, sets itself as the controller, and wraps the result in a {@link Scene}.
 *
 * @param <T> the type of controller associated with this view
 */
public abstract class View <T extends Controller> {

    /** the Controller */
    protected T controller;

    /** The root layout pane loaded from FXML. */
    protected final Pane root;

    /** The JavaFX scene containing the root pane. */
    protected final Scene scene;

    /**
     * Loads the FXML at the given path and sets this instance as its controller.
     *
     * @param fxmlPath classpath-relative path to the FXML file (e.g. {@code "/fxml/myview.fxml"})
     * @throws IOException if the FXML file cannot be found or loaded
     */
    public View(String fxmlPath) throws IOException {
        URL url = View.class.getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController((Object) this);
        this.root = loader.load();
        this.scene = new Scene(root);
    }

    /**
     * Sets this view's scene on the given stage and shows it if not already visible.
     *
     * @param stage the JavaFX stage to display the view on
     */
    public void show(Stage stage) {
        stage.setScene(this.scene);
        stage.setTitle(this.getTitle());
        if (!stage.isShowing()) {
            stage.show();
        }
    }

    /**
     * @param controller the controller to delegate actions to
     */
    public void setController(T controller) {
        this.controller = controller;
    }

    protected Optional<String> showAndWaitTextInputDialog(TeamBugemonDTO selected, String title, String headerText, String contentText) {
        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setGraphic(null);
        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/common.css")).toExternalForm()
        );
        return dialog.showAndWait();
    }

    /**
     * @return the window title to display when this view is shown
     */
    protected abstract String getTitle();
}