package views.selection;

import controllers.SelectionController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import models.bugemon.dto.BugemonDTO;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

/**
 * Sub-view managing the team panel on the right side of the selection screen.
 * Displays the current team, stats label, and action buttons.
 * Included in selectionview.fxml via fx:include.
 */
public class TeamPanelView {

    private SelectionController controller;

    @FXML private GridPane teamGrid;
    @FXML private AnchorPane attackPanel;      // change ici en AnchorPane
    @FXML private VBox teamRectangle;
    @FXML private Label bugemonStatsLabel;
    @FXML private Button confirmButton;
    @FXML private Button startGameButton;
    @FXML private Button backButton;

    private static final int MIN_TEAM_SIZE = 1;

    /**
     * Called automatically by JavaFX after FXML injection.
     * Wires button actions.
     */
    @FXML
    public void initialize() {
        backButton.setOnAction(e -> controller.goToTitleScreen());
        confirmButton.setOnAction(e -> onConfirmClicked());
        startGameButton.setOnAction(e -> onStartGameClicked());
    }

    /**
     * Sets the controller. Called by SelectionView after FXML loading.
     *
     * @param controller the selection controller
     */
    public void setController(SelectionController controller) {
        this.controller = controller;
    }

    /**
     * Called when a Bugémon is added to the team.
     * Updates the team grid display.
     *
     * @param bugemon the Bugémon that was added
     */
    public void onBugemonAdded(BugemonDTO bugemon) {
        refreshTeamGrid();
    }

    /**
     * Called when a Bugémon is removed from the team.
     * Updates the team grid display.
     *
     * @param bugemon the Bugémon that was removed
     */
    public void onBugemonRemoved(BugemonDTO bugemon) {
        refreshTeamGrid();
    }

    /**
     * Updates the stats label with the given Bugémon's stats.
     *
     * @param bugemon the Bugémon to display stats for
     */
    public void showBugemonStats(BugemonDTO bugemon) {
        bugemonStatsLabel.setText(String.format(
                "%s | PV: %s | ATK: %s | DEF: %s | INIT: %s",
                bugemon.getNom(),
                bugemon.getPv(),
                bugemon.getAttaque(),
                bugemon.getDefense(),
                bugemon.getInitiative()
        ));
    }

    /**
     * Refreshes the team grid with the current player team's sprites.
     */
    public void refreshTeamGrid() {
        teamGrid.getChildren().clear();
        List<BugemonDTO> currentTeam = controller.getPlayerTeam().getTeamDTO();
        for (int i = 0; i < currentTeam.size(); i++) {
            BugemonDTO bugemon = currentTeam.get(i);
            String path = "/Assets/png/" + bugemon.getSprite();
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(60);
            imgView.setFitWidth(60);
            imgView.setPreserveRatio(true);
            teamGrid.add(imgView, i % 3, i / 3);
        }
    }

    /** Resets the panel to its initial state. */
    public void reset() {
        teamGrid.getChildren().clear();
        bugemonStatsLabel.setText("Clique sur un Bugémon pour voir ses statistiques");
    }

    private void onConfirmClicked() {
        if (controller.getPlayerTeam().getSize() < MIN_TEAM_SIZE) {
            showError("Aucun Bugémon", "Sélectionnez au moins 1 Bugémon avant de valider.");
            return;
        }
        if (controller.isEditing()) {
            controller.confirmSelection(controller.getPlayerTeam(), null);
        } else {
            confirmCreation();
        }
    }

    private void confirmCreation() {
        TextInputDialog dialog = new TextInputDialog("Mon équipe");
        dialog.setTitle("Sauvegarder l'équipe");
        dialog.setHeaderText("Donnez un nom à votre équipe (" + controller.getPlayerTeam().getSize() + " Bugémon(s))");
        dialog.setContentText("Nom :");
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setGraphic(null);
        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/common.css")).toExternalForm()
        );
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().trim().isEmpty()) return;
        controller.confirmSelection(controller.getPlayerTeam(), result.get().trim());
    }

    void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/common.css")).toExternalForm()
        );
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setGraphic(null);
        alert.showAndWait();
    }

    private void onStartGameClicked(){
        if (controller.getPlayerTeam().getSize()<1){
            showError("Equipe vide", //titre
                    "Il faut au moins 1 Bogémon pour pouvoir lancer le jeu." //message
            );
            return;
        }
        controller.goToFight();
    }
}