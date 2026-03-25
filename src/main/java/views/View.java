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

import controllers.Controller;
import models.bugemon.dto.BugemonDTO;
import models.bugemon.dto.TeamBugemonDTO;
import views.utils.PopupManager;

/**
 * Abstract base class for all views.
 * Loads an FXML file, sets itself as the controller, and wraps the result in a {@link Scene}.
 * <p>
 * Popup logic is delegated to a {@link PopupManager} instance.
 * Subclasses can call the convenience methods directly, or access
 * {@link #popup} for finer control. {@link PopupManager} is user by View and SubView classes.
 *
 * @param <T> the type of controller associated with this view
 */
public abstract class View<T extends Controller> {


    /** Shared popup helper — use its methods or access raw popups via getters. */
    protected final PopupManager popup = new PopupManager();

    /**
     * Convenience alias kept for backward compatibility with subclasses
     * that reference {@code HPPopup} directly (e.g. {@code HPPopup.hide()}).
     */
    protected final Popup HPPopup    = popup.getHpPopup();

    /**
     * Convenience alias kept for backward compatibility with subclasses
     * that reference {@code statsPopup} directly.
     */
    protected final Popup statsPopup = popup.getStatsPopup();


    /** The controller associated with this view. */
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
        loader.setController(this);
        this.root  = loader.load();
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
     * Attaches this view's scene and title to the given stage without showing it.
     * <p>
     * This method is mainly used by the meta-controller when opening sub-scenes
     * in secondary windows.
     * </p>
     *
     * @param stage the stage to configure with this view
     */
    public void attachTo(Stage stage) {
        stage.setScene(this.scene);
        stage.setTitle(this.getTitle());
    }

    /**
     * Closes the window currently displaying this view, if any.
     * <p>
     * This is typically used by sub-view controllers to close modal windows
     * without directly manipulating the stage.
     * </p>
     */
    public void closeWindow() {
        Window window = scene.getWindow();
        if (window instanceof Stage stage) {
            stage.close();
        }
    }

    /** @return the window title to display when this view is shown */
    protected abstract String getTitle();


    /** @param controller the controller to delegate actions to */
    public void setController(T controller) {
        this.controller = controller;
    }


    /**
     * Shows the HP popup next to the cursor for the given Bugémon.
     *
     * @param event  the mouse event carrying screen coordinates
     * @param bugemon the Bugémon whose HP to display
     * @param node   the node used as owner for the popup
     */
    protected void showHpPopup(MouseEvent event, BugemonDTO bugemon, ImageView node) {
        popup.showHpPopup(event, bugemon, node);
    }

    /**
     * Moves the HP popup to follow the cursor.
     *
     * @param event the latest mouse-moved event
     */
    protected void updatePopupPosition(MouseEvent event) {
        popup.updateHpPosition(event);
    }

    /**
     * Shows the stats popup at the given screen position for the given Bugémon.
     *
     * @param x      screen X of the triggering event
     * @param y      screen Y of the triggering event
     * @param bugemon the Bugémon whose stats to display
     * @param node   the node used as owner for the popup
     */
    protected void showStatsPopup(double x, double y, BugemonDTO bugemon, ImageView node) {
        popup.showStatsPopup(x, y, bugemon, node);
    }


    /**
     * Shows a styled {@link TextInputDialog} pre-filled with the team's current name.
     *
     * @param selected    the team being renamed
     * @param title       dialog title
     * @param headerText  dialog header
     * @param contentText label before the text field
     * @return the string entered by the user, or empty if cancelled
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