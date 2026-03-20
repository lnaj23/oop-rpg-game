package views.selection;

import controllers.SelectionController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import javafx.scene.control.Label;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import services.fight.TypeEffectivenessService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Sub-view managing the Bugémon selection grid.
 * Handles button display, hover popups and click-to-select logic.
 * Included in selectionview.fxml via fx:include.
 */
public class BugemonGridView {

    private SelectionController controller;

    @FXML private GridPane gridPane;

    /** Popup shown on hover to preview a Bugémon's stats. */
    private final Popup statsPopup = new Popup();
    private final Label statsLabel = new Label();

    /** Bugémons currently displayed in the grid, in order. */
    private final List<BugemonDTO> displayedBugemons = new ArrayList<>();

    /** Grid buttons that are currently selected. */
    private final List<Button> selectedButtons = new ArrayList<>();

    /** Maximum number of Bugémons allowed in a team. */
    private static final int MAX_TEAM_SIZE = 6;

    /**
     * Called automatically by JavaFX after FXML injection.
     * Initializes the stats popup style.
     */
    @FXML
    public void initialize() {
        statsLabel.setMouseTransparent(true);
        statsLabel.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; -fx-padding: 5; -fx-background-radius: 5;");
        statsPopup.getContent().add(statsLabel);
        statsPopup.setAutoHide(true);
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
     * Populates the grid with all available Bugémons.
     * Sets up hover popups and click-to-toggle for each button.
     *
     * @param allBugemons the list of all available Bugémons
     */
    public void displayBugemons(List<BugemonDTO> allBugemons) {
        List<Node> buttons = gridPane.getChildren().filtered(n -> n instanceof Button);
        for (int i = 0; i < allBugemons.size() && i < buttons.size(); i++) {
            BugemonDTO bugemon = allBugemons.get(i);
            Button btn = (Button) buttons.get(i);
            displayedBugemons.add(bugemon);
            btn.setText(bugemon.getName());
            initImage(bugemon, btn);
            btn.setOnMouseEntered(event -> refreshLabel(event, bugemon, btn));
            btn.setOnMouseMoved(this::labelUpdatePosition);
            btn.setOnMouseExited(event -> statsPopup.hide());
            final int index = i;
            btn.setOnAction(e -> bugemonOnClick(btn, index));
            //right click to unselect every bugemon
            btn.setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.SECONDARY) {
                    controller.reset();
                }
            });
        }
    }

    /**
     * Pre-selects the Bugémons of an existing team in the grid.
     *
     * @param teamBugemons     the team to pre-select
     * @param listener the panel that listens for team changes
     */
    public void preSelectTeam(List<BugemonDTO> teamBugemons, TeamPanelView listener) {
        List<Node> buttons = gridPane.getChildren().filtered(n -> n instanceof Button);
        for (int i = 0; i < displayedBugemons.size() && i < buttons.size(); i++) {
            BugemonDTO currentBug = displayedBugemons.get(i);
            if (teamBugemons.stream().anyMatch(b -> b.getId().equals(currentBug.getId()))) {
                Button btn = (Button) buttons.get(i);
                selectedButtons.add(btn);
                listener.onBugemonAdded(currentBug);
            }
        }
    }

    /**
     * Clears all selections and reloads the grid.
     *
     * @param allBugemons the full list of Bugémons to reload
     */
    public void reset(List<BugemonDTO> allBugemons) {
        for (Button btn : selectedButtons) btn.setStyle("");
        selectedButtons.clear();
        displayedBugemons.clear();
        displayBugemons(allBugemons);
    }

    private void bugemonOnClick(Button btn, int index) {
        BugemonDTO selected = displayedBugemons.get(index);
        boolean wasSelected = selectedButtons.contains(btn);
        controller.onBugemonClicked(btn, selected, wasSelected, selectedButtons.size() >= MAX_TEAM_SIZE);
    }

    /**
     * Selects a button visually.
     *
     * @param btn the button to select
     */
    public void selectButton(Button btn, BugemonDTO bugemon) {
        selectedButtons.add(btn);
        String typeColor = bugemon.getColor();

        btn.setStyle(
                "-fx-background-color: derive(" + typeColor + ", -50%); " +
                        "-fx-border-color: derive(" + typeColor + ", -80%); " +
                        "-fx-border-width: 2;"
        );

    }

    /**
     * Deselects a button visually.
     *
     * @param btn the button to deselect
     */
    public void deselectButton(Button btn) {
        selectedButtons.remove(btn);
        BugemonDTO bugemon = (BugemonDTO) btn.getUserData();

        if (bugemon != null) {
            String typeColor = bugemon.getColor();

            btn.setStyle(
                    "-fx-background-color: " + typeColor + "; " +
                            "-fx-border-color: derive(" + typeColor + ", -30%); " +
                            "-fx-border-width: 2;"
            );
        }
    }

    private void initImage(BugemonDTO bugemon, Button btn) {
        String path = "/Assets/png/" + bugemon.getSprite();
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(50);
        imgView.setFitWidth(50);
        imgView.setPreserveRatio(true);
        btn.setGraphic(imgView);
        btn.setContentDisplay(ContentDisplay.TOP);
        String typeColor = bugemon.getColor();

        btn.setStyle(
                "-fx-background-color: " + typeColor + "; " +
                        "-fx-border-color: derive(" + typeColor + ", -30%); " +
                        "-fx-border-width: 2;"
        );
    }

    private void refreshLabel(MouseEvent event, BugemonDTO bugemon, Button btn) {
        statsLabel.setText(controller.buildStatsText(bugemon));
        statsPopup.show(btn, event.getScreenX() + 15, event.getScreenY() + 15);
    }

    private void labelUpdatePosition(MouseEvent event) {
        statsPopup.setX(event.getScreenX() + 15);
        statsPopup.setY(event.getScreenY() + 15);
    }
}