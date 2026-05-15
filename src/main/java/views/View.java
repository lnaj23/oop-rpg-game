package views;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import configuration.AppConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import models.bugemon.dto.BugemonDTO;
import models.bugemon.dto.TeamBugemonDTO;
import services.view.popup.PopupManager;

/**
 * Abstract base class for all JavaFX views.
 * <p>
 * This class only handles common view responsibilities: loading the FXML,
 * storing the root pane, creating the scene, showing/attaching the scene to a
 * stage, and providing shared popup helpers.
 * </p>
 * <p>
 * It deliberately does not know any controller type. Each concrete view must
 * explicitly declare the controller or listener it needs.
 * </p>
 */
public abstract class View {

    /** Shared popup helper — use its methods or access raw popups via getters. */
    protected final PopupManager popup = new PopupManager();

    /**
     * Convenience alias kept for subclasses that reference {@code HPPopup}
     * directly.
     */
    protected final Popup HPPopup = popup.getHpPopup();

    /** Convenience alias kept for subclasses that reference {@code statsPopup}. */
    protected final Popup statsPopup = popup.getStatsPopup();

    /** The root layout pane loaded from FXML. */
    protected final Pane root;

    /** The JavaFX scene containing the root pane. */
    protected final Scene scene;

    /**
     * Loads the FXML at the given path and sets this view instance as the FXML
     * controller.
     *
     * @param fxmlPath classpath-relative path to the FXML file
     * @throws IOException if the FXML file cannot be found or loaded
     */
    public View(String fxmlPath) throws IOException {
        URL url = View.class.getResource(fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        this.root = loader.load();
        this.scene = new Scene(root);
    }

    /**
     * Sets this view's scene on the given stage and shows it if not already
     * visible.
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
     * Attaches this view's scene and title to the given stage without showing it.
     *
     * @param stage the stage to configure with this view
     */
    public void attachTo(Stage stage) {
        stage.setScene(this.scene);
        stage.setTitle(this.getTitle());
    }

    /** Closes the window currently displaying this view, if any. */
    public void closeWindow() {
        Window window = scene.getWindow();
        if (window instanceof Stage stage) {
            stage.close();
        }
    }

    /** @return the window title to display when this view is shown */
    protected abstract String getTitle();

    /**
     * Shows the HP popup next to the cursor for the given Bugémon.
     *
     * @param event the mouse event carrying screen coordinates
     * @param bugemon the Bugémon whose HP to display
     * @param node the node used as owner for the popup
     */
    protected void showHpPopup(MouseEvent event, BugemonDTO bugemon, ImageView node) {
        popup.showHpPopup(event, bugemon, node);
    }

    /** Moves the HP popup to follow the cursor. */
    protected void updatePopupPosition(MouseEvent event) {
        popup.updateHpPosition(event);
    }

    /**
     * Shows a styled text input dialog pre-filled with the team's current name.
     *
     * @param selected the team being renamed
     * @param title dialog title
     * @param headerText dialog header
     * @param contentText label before the text field
     * @return the string entered by the user, or empty if canceled
     */
    protected Optional<String> showAndWaitTextInputDialog(
            TeamBugemonDTO selected, String title, String headerText, String contentText) {

        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        dialog.setContentText(contentText);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setGraphic(null);
        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(AppConfig.CSS_COMMON)).toExternalForm()
        );
        return dialog.showAndWait();
    }
}
