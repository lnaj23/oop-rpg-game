package views;

import configuration.AppConfig;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

import java.util.Objects;

/**
 * Utility class for displaying fatal error messages.
 * Shows a blocking error dialog before the application exits.
 */
public class FatalErrorView {

    /**
     * Displays a modal error dialog with the given message.
     *
     * @param message the error message to display
     */
    public static void show(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setGraphic(null);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(FatalErrorView.class.getResource(AppConfig.CSS_COMMON)).toExternalForm()
        );
        alert.showAndWait();
    }
}