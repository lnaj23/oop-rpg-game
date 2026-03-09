package views;

import controllers.SelectionController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.StageStyle;
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
public class SelectionView extends View <SelectionController> {


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

    /** Maximum number of Bugemons allowed in a team. */
    private static final int MAX_TEAM_SIZE = 6;

    /** Minimum number of Bugemons required in a team. */
    private static final int MIN_TEAM_SIZE = 1;

    /**
     * Loads the FXML, sets up the hover popup, and wires button actions.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public SelectionView() throws IOException {
        super("/fxml/selectionview.fxml");

        initLabel();

        backButton.setOnAction(e -> this.controller.goToTitleScreen());
        confirmButton.setOnAction(e -> onConfirmClicked());

        displayBugemons();
    }

    /**
     * Initializes the stats label style and adds it to the popup.
     */
    private void initLabel() {
        statsLabel.setMouseTransparent(true);
        statsLabel.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 5;");
        statsPopup.getContent().add(statsLabel);
        statsPopup.setAutoHide(true);
    }

    /**
     * Sets the player's team. Called by the controller.
     *
     * @param team the team to assign to the player
     */
    public void setPlayerTeam(TeamBugemonDTO team) {
        this.playerTeam = team;
    }

    /**
     * Populates the Bugémon grid with all available Bugémons.
     * Sets up hover popups and click-to-toggle selection for each button.
     *
     * @throws RuntimeException if the image of a Bugemon could not be found
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
                initImage(bugemon, btn);
            } catch (Exception e) {
                throw new RuntimeException("Image not found for Bugemon : " + bugemon.getNom());
            }

            btn.setOnMouseEntered(event -> refreshLabel(event, bugemon, btn));

            btn.setOnMouseMoved(this::labelUpdatePosition);

            btn.setOnMouseExited(event -> statsPopup.hide());

            final int index = i;
            btn.setOnAction(e -> bugemonOnClick(btn, index));
        }
    }

    /**
     * Creates an ImageView for a given Bugemon sprite.
     *
     * @param bugemon the Bugemon whose sprite will be loaded
     * @param size    the width and height of the image
     * @return the configured ImageView
     * @throws RuntimeException if the image could not be found
     */
    private ImageView createImageView(BugemonDTO bugemon, int size) {
        String path = "/Assets/png/" + bugemon.getSprite();
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(size);
        imgView.setFitWidth(size);
        imgView.setPreserveRatio(true);
        return imgView;
    }

    /**
     * Initializes and sets the image of a Bugemon on its button.
     *
     * @param bugemon the Bugemon whose sprite will be displayed
     * @param btn     the button on which the image will be set
     */
    private void initImage(BugemonDTO bugemon, Button btn) {
        ImageView imgView = createImageView(bugemon, 50);
        btn.setGraphic(imgView);
        btn.setContentDisplay(ContentDisplay.TOP);
    }

    /**
     * Updates the stats label content and displays the popup next to the cursor.
     *
     * @param event   the mouse event used to get the cursor position
     * @param bugemon the Bugemon whose stats will be displayed
     * @param btn     the button that triggered the event
     */
    private void refreshLabel(MouseEvent event, BugemonDTO bugemon, Button btn) {
        String stats = "PV: "       + bugemon.getPv()              + "\n"
                + "ATK: "      + bugemon.getAttaque()         + "\n"
                + "DEF: "      + bugemon.getDefense()         + "\n"
                + "INIT: "     + bugemon.getInitiative()      + "\n"
                + "Attaques: " + bugemon.getAttackNamesAsString();
        statsLabel.setText(stats);
        statsPopup.show(btn, event.getScreenX() + 15, event.getScreenY() + 15);
    }

    /**
     * Updates the position of the stats popup to follow the cursor.
     *
     * @param event the mouse event used to get the current cursor position
     */
    private void labelUpdatePosition(MouseEvent event) {
        statsPopup.setX(event.getScreenX() + 15);
        statsPopup.setY(event.getScreenY() + 15);
    }

    /**
     * Handles the click event on a Bugemon button.
     * Toggles the selection of the button and displays the Bugemon's stats if it was not already selected.
     *
     * @param btn   the button that was clicked
     * @param index the index of the Bugemon in the displayed list
     */
    private void bugemonOnClick(Button btn, int index) {
        BugemonDTO selected = displayedBugemons.get(index);
        boolean wasSelected = selectedButtons.contains(btn);
        toggleSelection(btn, selected);
        if (!wasSelected) showBugemonStats(selected);
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
            if (selectedButtons.size() >= MAX_TEAM_SIZE) {
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
                bugemon.getPv(),
                bugemon.getAttaque(),
                bugemon.getDefense(),
                bugemon.getInitiative()
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
                ImageView imgView = createImageView(bugemon, 60);
                teamGrid.add(imgView, i % 3, i / 3);
            } catch (Exception e) {
                throw new RuntimeException("Image not found for Bugemon : " + bugemon.getNom());
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
        if (playerTeam.getSize() < MIN_TEAM_SIZE) {
            showError("Aucun Bugémon", "Sélectionnez au moins 1 Bugémon avant de valider.");
            return;
        }
        if (this.controller.isEditing()) {
            confirmEdit();
        } else {
            confirmCreation();
        }
    }

    /**
     * Confirms the selection in edit mode, keeping the existing team name.
     */
    private void confirmEdit() {
        this.controller.confirmSelection(playerTeam, null);
    }

    /**
     * Prompts the user for a team name and confirms the selection in creation mode.
     */
    private void confirmCreation() {
        TextInputDialog dialog = new TextInputDialog("Mon équipe");
        dialog.setTitle("Sauvegarder l'équipe");
        dialog.setHeaderText("Donnez un nom à votre équipe (" + playerTeam.getSize() + " Bugémon(s))");
        dialog.setContentText("Nom :");
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setGraphic(null);
        dialog.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/common.css")).toExternalForm()
        );
        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().trim().isEmpty()) return;
        this.controller.confirmSelection(playerTeam, result.get().trim());
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
        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/common.css")).toExternalForm()
        );
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setGraphic(null);
        alert.showAndWait();
    }

    @Override
    protected String getTitle() {
        return "Sélection d'équipe - Bugémon";
    }
}