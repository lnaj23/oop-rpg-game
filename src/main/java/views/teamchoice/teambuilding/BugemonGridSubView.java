package views.teamchoice.teambuilding;

import configuration.AppConfig;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import models.bugemon.dto.BugemonDTO;
import services.view.popup.PopupManager;
import services.view.assets.SpriteLoader;
import services.view.style.TypeColorService;
import views.listener.TeamBuilderListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sub-view managing the Bugémon selection grid.
 * Handles button display, hover/click popups and selection logic.
 * Loaded via {@code fx:include} in {@code selectionview.fxml} — does not extend {@link views.View}.
 *
 * <p>
 * Popup functionality is provided by a {@link PopupManager} instance (composition).
 * </p>
 *
 * <p>
 * This sub-view only talks to a {@link TeamBuilderListener} — never to a concrete controller.
 * </p>
 */
public class BugemonGridSubView {

    private TeamBuilderListener listener;

    @FXML private GridPane gridPane;

    /** Popup helper — provides HP and stats popups without requiring View inheritance. */
    private final PopupManager popup = new PopupManager();

    /** Bugémons currently displayed in the grid, in insertion order. */
    private final List<BugemonDTO> displayedBugemons = new ArrayList<>();

    /** Buttons that are currently selected. */
    private final List<Button> selectedButtons = new ArrayList<>();

    /** Maximum number of Bugémons allowed in a team. */
    private static final int MAX_TEAM_SIZE = AppConfig.MAX_TEAM_SIZE;


    /**
     * Sets the listener. Called by {@link TeamBuilderView} after FXML loading.
     *
     * @param listener the listener that will receive user events
     */
    public void setListener(TeamBuilderListener listener) {
        this.listener = listener;
    }


    /**
     * Populates the grid with all available Bugémons.
     * Sets up hover popups and click-to-toggle for each button.
     *
     * @param allBugemons the list of all available Bugémons
     */
    public void displayBugemons(List<BugemonDTO> allBugemons) throws IOException {
        gridPane.getChildren().clear();
        gridPane.getColumnConstraints().clear();

        int cols = 6;
        for (int c = 0; c < cols; c++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / cols);
            gridPane.getColumnConstraints().add(cc);
        }

        for (int i = 0; i < allBugemons.size(); i++) {
            BugemonDTO bugemon = allBugemons.get(i);
            Button btn = new Button();
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setMaxHeight(Double.MAX_VALUE);
            GridPane.setHgrow(btn, Priority.ALWAYS);
            GridPane.setFillWidth(btn, true);
            displayedBugemons.add(bugemon);
            setupButton(btn, bugemon, i);
            gridPane.add(btn, i % cols, i / cols);
        }
    }

    /**
     * Pre-selects the Bugémons of an existing team in the grid.
     *
     * @param teamBugemons the team to pre-select
     * @param panel        the team panel that needs to be notified for each added Bugémon
     */
    public void preSelectTeam(List<BugemonDTO> teamBugemons, TeamPanelSubView panel) {
        List<Node> buttons = gridPane.getChildren().filtered(n -> n instanceof Button);
        for (int i = 0; i < displayedBugemons.size() && i < buttons.size(); i++) {
            BugemonDTO current = displayedBugemons.get(i);
            if (teamBugemons.stream().anyMatch(b -> b.getId().equals(current.getId()))) {
                Button btn = (Button) buttons.get(i);
                selectButton(btn, current);
                panel.onBugemonAdded(current);
            }
        }
    }

    /**
     * Clears all selections and reloads the grid.
     *
     * @param allBugemons the full list of Bugémons to reload
     */
    public void reset(List<BugemonDTO> allBugemons) throws IOException {
        selectedButtons.forEach(btn -> btn.setStyle(""));
        selectedButtons.clear();
        displayedBugemons.clear();
        displayBugemons(allBugemons);
    }

    /**
     * Selects a button visually, applying a darkened type-colour style.
     *
     * @param btn    the button to select
     * @param bugemon the Bugémon associated with the button
     */
    public void selectButton(Button btn, BugemonDTO bugemon) {
        selectedButtons.add(btn);
        String color = TypeColorService.getColor(bugemon.getType());
        btn.setStyle(
                "-fx-background-color: derive(" + color + ", -50%); " +
                        "-fx-border-color: derive("     + color + ", -80%); " +
                        "-fx-border-width: 2;"
        );
    }

    /**
     * Deselects a button visually, restoring the base type-colour style.
     *
     * @param btn the button to deselect
     */
    public void deselectButton(Button btn) {
        selectedButtons.remove(btn);
        BugemonDTO bugemon = (BugemonDTO) btn.getUserData();
        if (bugemon != null) {
            String color = TypeColorService.getColor(bugemon.getType());
            btn.setStyle(
                    "-fx-background-color: " + color + "; " +
                            "-fx-border-color: derive(" + color + ", -30%); " +
                            "-fx-border-width: 2;"
            );
        }
    }


    private void setupButton(Button btn, BugemonDTO bugemon, int index) throws IOException {
        btn.setText(bugemon.getName());
        applyButtonStyle(btn, bugemon);

        btn.setOnMouseEntered(e -> popup.showStatsPopup(e.getScreenX(), e.getScreenY(), bugemon, btn));
        btn.setOnMouseMoved(e  -> popup.updateStatsPosition(e));
        btn.setOnMouseExited(e -> popup.hideStats());

        btn.setOnAction(e -> onBugemonClicked(btn, index));
        btn.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY && listener != null) listener.onReset();
        });
    }

    private void onBugemonClicked(Button btn, int index) {
        if (listener == null) return;
        BugemonDTO selected   = displayedBugemons.get(index);
        boolean    wasSelected = selectedButtons.contains(btn);
        listener.onBugemonClicked(btn, selected, wasSelected, selectedButtons.size() >= MAX_TEAM_SIZE);
    }

    private void applyButtonStyle(Button btn, BugemonDTO bugemon) throws IOException {
        Image img = SpriteLoader.loadSprite(bugemon);
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(50);
        imgView.setFitWidth(50);
        imgView.setPreserveRatio(true);

        btn.setGraphic(imgView);
        btn.setContentDisplay(ContentDisplay.TOP);
        btn.setUserData(bugemon);

        String color = TypeColorService.getColor(bugemon.getType());
        btn.setStyle(
                "-fx-background-color: " + color + "; " +
                        "-fx-border-color: derive(" + color + ", -30%); " +
                        "-fx-border-width: 2;"
        );
    }
}