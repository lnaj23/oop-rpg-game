package views;

import controllers.SelectionController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import models.Bugemon.DTO.BugemonDTO;
import services.BugemonProvider;
import models.Bugemon.DTO.TeamBugemonDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * View for the Bugémon selection screen.
 * Displays a grid of all available Bugémons and lets the player build a team of up to 6.
 * Supports both new team creation and editing of an existing team.
 */
public class SelectionView extends View {

    private SelectionController controller;

    @FXML private GridPane gridPane;
    @FXML private GridPane teamGrid;
    @FXML private VBox teamRectangle;
    @FXML private Label bugemonStatsLabel;
    @FXML private Button confirmButton;
    @FXML private Button backButton;

    /** Popup shown on hover to preview a Bugémon's stats. */
    private final Popup statsPopup = new Popup();
    private final Label statsLabel = new Label();

    /** Bugémons currently displayed in the grid, in order. */
    private final List<BugemonDTO> displayedBugemons = new ArrayList<>();

    /** Grid buttons that are currently selected (highlighted in red). */
    private final List<Button> selectedButtons = new ArrayList<>();

    /** The team being built by the player. */
    private TeamBugemonDTO playerTeam;

    /**
     * Loads the FXML, sets up the hover popup, and wires button actions.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public SelectionView() throws IOException {
        super("/fxml/selectionview.fxml");

        statsLabel.setMouseTransparent(true);
        statsLabel.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 5;");
        statsPopup.getContent().add(statsLabel);
        statsPopup.setAutoHide(true);

        backButton.setOnAction(e -> this.controller.goToMenu());
        confirmButton.setOnAction(e -> onConfirmClicked());

        displayBugemons();
    }

    /**
     * @param controller the controller to delegate actions to
     */
    public void setController(SelectionController controller) {
        this.controller = controller;
    }

    public void setPlayerTeam(TeamBugemonDTO team) {
        this.playerTeam = team;
    }

    /**
     * Populates the Bugémon grid with all available Bugémons.
     * Sets up hover popups and click-to-toggle selection for each button.
     */
    private void displayBugemons() {
        List<BugemonDTO> allBugemons = BugemonProvider.getAllBugemonsDTO();
        List<Node> buttons = gridPane.getChildren().filtered(node -> node instanceof Button);

        for (int i = 0; i < allBugemons.size() && i < buttons.size(); i++) {
            BugemonDTO bugemon = allBugemons.get(i);
            Button btn = (Button) buttons.get(i);
            displayedBugemons.add(bugemon);
            btn.setText(bugemon.getNom());

            try {
                String path = "/Assets/png/" + bugemon.getSprite();
                Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(50);
                imgView.setPreserveRatio(true);
                btn.setGraphic(imgView);
                btn.setContentDisplay(ContentDisplay.TOP);
            } catch (Exception e) {
                System.err.println("Impossible de charger l'image pour : " + bugemon.getNom());
            }

            btn.setOnMouseEntered(event -> {
                String stats = "PV: " + BugemonProvider.getSTATS(bugemon.getId(), "pv") + "\n"
                        + "ATK: " + BugemonProvider.getSTATS(bugemon.getId(), "attaque") + "\n"
                        + "DEF: " + BugemonProvider.getSTATS(bugemon.getId(), "defense") + "\n"
                        + "INIT: " + BugemonProvider.getSTATS(bugemon.getId(), "initiative") + "\n"
                        + "Attaques: " + BugemonProvider.getSTATS(bugemon.getId(), "attaques");
                statsLabel.setText(stats);
                statsPopup.show(btn, event.getScreenX() + 15, event.getScreenY() + 15);
            });
            btn.setOnMouseMoved(event -> {
                statsPopup.setX(event.getScreenX() + 15);
                statsPopup.setY(event.getScreenY() + 15);
            });
            btn.setOnMouseExited(event -> statsPopup.hide());

            final int index = i;
            btn.setOnAction(e -> {
                BugemonDTO selected = displayedBugemons.get(index);
                toggleSelection(btn, selected);
                showBugemonStats(selected);
            });
        }
    }

    /**
     * Toggles the selection state of a Bugémon button.
     * Adds or removes the Bugémon from the player's team and updates button style.
     * Shows an error if the team is already full (6 Bugémons).
     *
     * @param btn the button that was clicked
     * @param bugemon the Bugémon associated with that button
     */
    private void toggleSelection(Button btn, BugemonDTO bugemon) {
        if (selectedButtons.contains(btn)) {
            selectedButtons.remove(btn);
            playerTeam.removeBugemonDTO(bugemon);
            btn.setStyle("");
        } else {
            if (selectedButtons.size() >= 6) {
                showError("Équipe pleine", "Vous ne pouvez sélectionner que 6 Bugémons maximum.");
                return;
            }
            selectedButtons.add(btn);
            playerTeam.addBugemonDTO(bugemon);
            btn.setStyle("-fx-background-color: #e8a8a8; -fx-border-color: #c0392b; -fx-border-width: 2;");
        }
        updateTeamGridDisplay();
    }

    /**
     * Updates the stats label at the bottom with the given Bugémon's stats.
     *
     * @param bugemon the Bugémon to display stats for
     */
    private void showBugemonStats(BugemonDTO bugemon) {
        bugemonStatsLabel.setText(String.format(
                "%s | PV: %s | ATK: %s | DEF: %s | INIT: %s",
                bugemon.getNom(),
                BugemonProvider.getSTATS(bugemon.getId(), "pv"),
                BugemonProvider.getSTATS(bugemon.getId(), "attaque"),
                BugemonProvider.getSTATS(bugemon.getId(), "defense"),
                BugemonProvider.getSTATS(bugemon.getId(), "initiative")
        ));
    }

    /**
     * Refreshes the team preview grid with the current player team's sprites.
     */
    private void updateTeamGridDisplay() {
        teamGrid.getChildren().clear();
        List<BugemonDTO> currentTeam = playerTeam.getTeamDTO();
        for (int i = 0; i < currentTeam.size(); i++) {
            BugemonDTO bugemon = currentTeam.get(i);
            try {
                String path = "/Assets/png/" + bugemon.getSprite();
                Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
                ImageView imgView = new ImageView(img);
                imgView.setFitHeight(60);
                imgView.setFitWidth(60);
                imgView.setPreserveRatio(true);
                teamGrid.add(imgView, i % 3, i / 3);
            } catch (Exception e) {
                System.err.println("Impossible de charger l'image miniature pour : " + bugemon.getNom());
            }
        }
    }

    /**
     * Pre-selects the Bugémons of an existing team in the grid (edit mode).
     * Selected buttons are highlighted in green.
     *
     * @param team the team whose Bugémons should be pre-selected
     */
    public void preSelectTeam(TeamBugemonDTO team) {
        List<Node> buttons = gridPane.getChildren().filtered(n -> n instanceof Button);
        for (int i = 0; i < displayedBugemons.size() && i < buttons.size(); i++) {
            BugemonDTO currentBug = displayedBugemons.get(i);
            if (team.getTeamDTO().stream().anyMatch(b -> b.getId().equals(currentBug.getId()))) {
                Button btn = (Button) buttons.get(i);
                selectedButtons.add(btn);
                playerTeam.addBugemonDTO(currentBug);
                btn.setStyle("-fx-background-color: #e8a8a8; -fx-border-color: #c0392b; -fx-border-width: 2;");
            }
        }
        updateTeamGridDisplay();
    }

    /**
     * Resets the view to its initial state: clears selection, team, stats label, and reloads the grid.
     */
    public void reset() {
        for (Button btn : selectedButtons) btn.setStyle("");
        selectedButtons.clear();
        controller.resetPlayerTeam();
        teamGrid.getChildren().clear();
        bugemonStatsLabel.setText("Clique sur un Bugémon pour voir ses statistiques");
        displayedBugemons.clear();
        displayBugemons();
    }

    /**
     * Handles the confirm button click. Shows an error if the team is empty.
     * In edit mode, confirms directly. In creation mode, prompts for a team name first.
     */
    private void onConfirmClicked() {
        if (playerTeam.getSize() < 1) {
            showError("Aucun Bugémon", "Sélectionnez au moins 1 Bugémon avant de valider.");
            return;
        }
        if (this.controller.isEditing()) {
            this.controller.confirmSelection(playerTeam, null);
        } else {
            TextInputDialog dialog = new TextInputDialog("Mon équipe");
            dialog.setTitle("Sauvegarder l'équipe");
            dialog.setHeaderText("Donnez un nom à votre équipe (" + playerTeam.getSize() + " Bugémon(s))");
            dialog.setContentText("Nom :");
            Optional<String> result = dialog.showAndWait();
            if (!result.isPresent() || result.get().trim().isEmpty()) return;
            this.controller.confirmSelection(playerTeam, result.get().trim());
        }
    }

    /**
     * Displays a modal error dialog.
     *
     * @param title the dialog title
     * @param message the error message
     */
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    protected String getTitle() {
        return "Sélection d'équipe - Bugémon";
    }
}