package views;

import javafx.scene.control.Alert;

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
        alert.setTitle("Fatal error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}