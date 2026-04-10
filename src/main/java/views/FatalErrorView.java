package views;

import configuration.AppConfig;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
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
        alert.setHeaderText(null);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setStyle(
                "-fx-background-color: #1a1a2e;" +
                        "-fx-border-color: #e24b4a;" +
                        "-fx-border-width: 2px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 20px;"
        );
        alert.getDialogPane().lookup(".content.label").setStyle(
                "-fx-text-fill: #f09595;" +
                        "-fx-font-size: 14px;" +
                        "-fx-wrap-text: true;"
        );
        alert.getDialogPane().lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: #e24b4a;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-padding: 6px 20px;" +
                        "-fx-cursor: hand;"
        );
        alert.showAndWait();
    }
}