package views.dialogs;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

/**
 * Dialog for saving a team with a custom name.
 * Provides text input validation and duplicate team name checking.
 */
public class SaveTeamDialog {

    private final TextField teamNameField;
    private final List<String> existingTeamNames;

    /**
     * Creates a new SaveTeamDialog.
     *
     * @param existingTeamNames list of existing team names to validate against (prevent duplicates)
     */
    public SaveTeamDialog(List<String> existingTeamNames) {
        this.existingTeamNames = existingTeamNames;
        this.teamNameField = new TextField();
    }

    /**
     * Shows the dialog and returns the entered team name if valid.
     * Returns empty Optional if cancelled or validation fails.
     *
     * @return Optional containing team name if valid, or empty if cancelled/invalid
     */
    public Optional<String> showAndWait() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Sauvegarder l'équipe");
        dialog.setHeaderText("Entrez le nom de votre équipe");

        teamNameField.setPromptText("Nom de l'équipe");
        teamNameField.setPrefWidth(300);

        // Create dialog content
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20; -fx-alignment: CENTER;");
        content.getChildren().addAll(
                new Label("Nom de l'équipe:"),
                teamNameField
        );

        dialog.getDialogPane().setContent(content);

        // Add buttons
        ButtonType saveButtonType = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, cancelButtonType);

        // Convert dialog result to team name
        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                return teamNameField.getText();
            }
            return null;
        });

        // Show dialog and get result
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String teamName = result.get().trim();

            // Validate: not empty
            if (teamName.isEmpty()) {
                showError("Le nom ne peut pas être vide!");
                return showAndWait();
            }

            // Validate: not a duplicate
            if (existingTeamNames.stream().anyMatch(name -> name.equalsIgnoreCase(teamName))) {
                showError("Une équipe avec ce nom existe déjà!");
                return showAndWait();
            }

            return Optional.of(teamName);
        }

        return Optional.empty();
    }

    /**
     * Shows an error alert dialog.
     *
     * @param message the error message to display
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Sauvegarde impossible");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
