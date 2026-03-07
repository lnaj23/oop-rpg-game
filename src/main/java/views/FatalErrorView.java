package views;

import javafx.scene.control.Alert;

public class FatalErrorView {

    public static void show(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fatal error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}