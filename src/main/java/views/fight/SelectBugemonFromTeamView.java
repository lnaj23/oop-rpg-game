package views.fight;

import controllers.Controller;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import listener.SelectBugemonFromTeamListener;
import models.bugemon.dto.BugemonDTO;
import views.View;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
/**
 * View for the Bugemon switch sub-window displayed during a fight.
 * <p>
 * This view renders the player's team, shows HP popups on hover,
 * and forwards selection and cancellation events to its associated
 * {@link Controller}.
 * </p>
 */
public class SelectBugemonFromTeamView extends View<Controller> {

    @FXML private GridPane grid;
    @FXML private Button   btnCancel;
    @FXML private Label    titleLabel;
    private SelectBugemonFromTeamListener listener;

    /**
     * Creates the Bugemon switch sub-view and loads its FXML layout.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public SelectBugemonFromTeamView() throws IOException {
        super("/fxml/fight/switchbugemonview.fxml");
    }

    /**
     * Returns the title of the Bugemon switch sub-window.
     *
     * @return the window title
     */
    @Override
    protected String getTitle() {
        return "Changer de Bugemon";
    }


    /**
     * Sets the listener for Bugemon selection and cancellation events.
     *
     * @param listener the {@link SelectBugemonFromTeamListener} that will handle
     *                 user interactions (Bugemon clicks and cancel button clicks)
     */
    public void setListener(SelectBugemonFromTeamListener listener) {
        this.listener = listener;
    }

    /**
     * Displays the given team in the switch grid and configures the cancel button.
     *
     * @param team the player's team to display
     * @param showCancel {@code true} if the cancel button should be visible,
     *                   {@code false} otherwise
     */
    public void displayTeam(List<BugemonDTO> team, boolean showCancel) {
        grid.getChildren().clear();

        for (int i = 0; i < team.size(); i++) {
            BugemonDTO bugemon = team.get(i);
            ImageView imgView = createSprite(bugemon);
            bindEvents(imgView, bugemon);
            grid.add(imgView, i % 3, i / 3);
        }

        btnCancel.setVisible(showCancel);
        btnCancel.setManaged(showCancel);

        if (showCancel) {
            btnCancel.setOnAction(e -> this.listener.onCancelClickedRequest());
            int row = (team.size() - 1) / 3 + 1;
            grid.add(btnCancel, 0, row);
            GridPane.setColumnSpan(btnCancel, 3);
            GridPane.setHalignment(btnCancel, HPos.CENTER);
            btnCancel.setMaxWidth(150);
        }
    }

    /**
     * Creates and configures a sprite ImageView for a Bugemon.
     * <p>
     * Loads the Bugemon's sprite image from the assets directory, sets fixed
     * dimensions (80x80), and applies a grayscale effect if the Bugemon is KO.
     * </p>
     *
     * @param bugemon the Bugemon for which to create the sprite
     * @return a configured ImageView displaying the Bugemon sprite
     */
    private ImageView createSprite(BugemonDTO bugemon) {
        Image img  = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/Assets/png/" + bugemon.getSprite())));
        ImageView view = new ImageView(img);
        view.setFitHeight(80);
        view.setFitWidth(80);
        view.setPreserveRatio(true);
        if (bugemon.getcurrenPv() <= 0) {
            view.setEffect(new ColorAdjust(0, -1, 0, 0));
        }
        return view;
    }

    /**
     * Binds hover and click events to a Bugemon sprite.
     * <p>
     * Hover displays the HP popup, while clicking delegates the selection
     * to the associated controller.
     * </p>
     *
     * @param imgView the sprite node
     * @param bugemon the Bugemon represented by the sprite
     */
    private void bindEvents(ImageView imgView, BugemonDTO bugemon) {
        imgView.setOnMouseEntered(e -> showHpPopup(e, bugemon, imgView));
        imgView.setOnMouseMoved(this::updatePopupPosition);
        imgView.setOnMouseExited(e -> popup.hideHp());
        imgView.setOnMouseClicked(e -> this.listener.onBugemonClickedRequest(bugemon));
    }

    /**
     * Shows an alert when the player clicks a KO or already on-field Bugémon.
     *
     * @param title   the alert title
     * @param message the alert message
     */
    public void showMessageKO(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setGraphic(null);
        alert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/common.css")).toExternalForm()
        );
        alert.showAndWait();
    }

}