package views.teamchoice.selection;

import configuration.AppConfig;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import models.bugemon.dto.BugemonDTO;
import services.view.assets.SpriteLoader;
import views.listener.SelectionListener;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Sub-view managing the team panel on the right side of the selection screen.
 * Displays the current team, stats label, and action buttons.
 * Included in selectionview.fxml via fx:include.
 *
 * <p>
 * This sub-view does not pull data from the controller — it receives
 * all data via method parameters (push model) and emits events through
 * a {@link SelectionListener}.
 * </p>
 *
 * <p>
 * It tracks locally the two pieces of state it needs for UX decisions:
 * {@code editing} (to decide whether to ask for a team name on confirm) and
 * {@code currentTeamSize} (to validate the minimum team size).
 * Both are pushed by the controller through {@link #setEditMode(boolean)} and
 * {@link #refreshTeamGrid(List)}.
 * </p>
 */
public class TeamPanelSubView {

    private SelectionListener listener;

    @FXML private GridPane teamGrid;
    @FXML private VBox teamRectangle;
    @FXML private Label bugemonStatsLabel;
    @FXML private Button confirmButton;
    @FXML private Button backButton;
    @FXML private Button playButton;
    @FXML private Button loadTeamButton;

    private static final int MIN_TEAM_SIZE = AppConfig.MIN_TEAM_SIZE;

    /** Tracked locally — set by {@link #refreshTeamGrid(List)}. */
    private int currentTeamSize = 0;

    /** Tracked locally — set by {@link #setEditMode(boolean)}. */
    private boolean editing = false;

    /**
     * Called automatically by JavaFX after FXML injection.
     * Wires button actions.
     */
    @FXML
    public void initialize() {
        backButton.setOnAction(e -> { if (listener != null) listener.onBack(); });
        confirmButton.setOnAction(e -> onConfirmClicked());
        playButton.setOnAction(e -> onPlayClicked());
        loadTeamButton.setOnAction(e -> { if (listener != null) listener.onLoadTeam(); });
        for (Button btn : List.of(confirmButton, loadTeamButton, playButton, backButton)) {
            btn.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.SECONDARY && listener != null) {
                    listener.onReset();
                }
            });
        }
    }

    /**
     * Sets the listener. Called by {@link SelectionView} after FXML loading.
     *
     * @param listener the listener that will receive user events
     */
    public void setListener(SelectionListener listener) {
        this.listener = listener;
    }

    /**
     * Called when a Bugémon is added to the team.
     * The grid is refreshed via {@link #refreshTeamGrid(List)} by the controller.
     *
     * @param bugemon the Bugémon that was added
     */
    public void onBugemonAdded(BugemonDTO bugemon) {
        // Le controller pousse la mise à jour via refreshTeamGrid
    }

    /**
     * Called when a Bugémon is removed from the team.
     * The grid is refreshed via {@link #refreshTeamGrid(List)} by the controller.
     *
     * @param bugemon the Bugémon that was removed
     */
    public void onBugemonRemoved(BugemonDTO bugemon) {
        // Le controller pousse la mise à jour via refreshTeamGrid
    }

    /**
     * Updates the stats label with the given Bugémon's stats.
     *
     * @param bugemon the Bugémon to display stats for
     */
    public void showBugemonStats(BugemonDTO bugemon) {
        bugemonStatsLabel.setText(String.format(
                "%s | PV: %s | ATK: %s | DEF: %s | INIT: %s",
                bugemon.getName(),
                bugemon.getPv(),
                bugemon.getAttaque(),
                bugemon.getDefense(),
                bugemon.getInitiative()
        ));
    }

    /**
     * Refreshes the team grid with the given list of Bugémons.
     * Called by the controller after each team modification (push model).
     *
     * @param team the updated list of Bugémons to display
     */
    public void refreshTeamGrid(List<BugemonDTO> team) throws IOException {
        this.currentTeamSize = team.size();
        teamGrid.getChildren().clear();
        for (int i = 0; i < team.size(); i++) {
            BugemonDTO bugemon = team.get(i);
            Image img = SpriteLoader.loadSprite(bugemon);
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(60);
            imgView.setFitWidth(60);
            imgView.setPreserveRatio(true);
            teamGrid.add(imgView, i % 3, i / 3);
        }
    }

    /** Resets the panel to its initial state. */
    public void reset() {
        this.currentTeamSize = 0;
        teamGrid.getChildren().clear();
        bugemonStatsLabel.setText("Cliquez sur un Bugémon pour voir ses statistiques");
    }

    /**
     * Handles the play button click.
     * Validates the team size before delegating to the listener.
     */
    private void onPlayClicked() {
        if (currentTeamSize < MIN_TEAM_SIZE) {
            showError("Aucun Bugémon", "Sélectionnez au moins 1 Bugémon avant de jouer.");
            return;
        }
        if (listener != null) listener.onPlay();
    }

    /**
     * Handles the confirm button click.
     * Validates the team size, then either delegates with a {@code null} name
     * in edit mode or opens the naming dialog for a new team.
     */
    private void onConfirmClicked() {
        if (currentTeamSize < MIN_TEAM_SIZE) {
            showError("Aucun Bugémon", "Sélectionnez au moins 1 Bugémon avant de valider.");
            return;
        }
        if (editing) {
            if (listener != null) listener.onConfirm(null);
        } else {
            confirmCreation();
        }
    }

    /**
     * Opens a text input dialog to name the new team, then delegates to the listener.
     */
    private void confirmCreation() {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Sauvegarder l'équipe");
        dialog.setHeaderText("Donnez un nom à votre équipe (" + currentTeamSize + " Bugémon(s))");
        dialog.setContentText("Nom :");
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setGraphic(null);
        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(AppConfig.CSS_COMMON)).toExternalForm()
        );
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().trim().isEmpty()) return;
        if (listener != null) listener.onConfirm(result.get().trim());
    }

    /**
     * Displays an error alert with the given title and message.
     *
     * @param title   the alert title
     * @param message the alert message
     */
    void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource(AppConfig.CSS_COMMON)).toExternalForm()
        );
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setGraphic(null);
        alert.showAndWait();
    }

    /**
     * Toggles edit mode visibility for action buttons.
     * Hides navigation and play buttons when editing an existing team.
     *
     * @param editing {@code true} if in edit mode
     */
    public void setEditMode(boolean editing) {
        this.editing = editing;
        loadTeamButton.setVisible(!editing);
        playButton.setVisible(!editing);
        backButton.setVisible(!editing);
    }
}