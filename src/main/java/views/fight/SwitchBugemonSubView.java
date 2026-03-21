package views.fight;

import controllers.fight.SwitchBugemonController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.bugemon.dto.BugemonDTO;
import views.View;
import views.utils.PopupManager;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Modal window for selecting the next on-field Bugémon.
 * Hover shows current HP via {@link PopupManager}, left-click selects.
 */
public class SwitchBugemonSubView extends View<SwitchBugemonController> {

    @FXML private GridPane grid;
    @FXML private Button   btnCancel;
    @FXML private Label    titleLabel;

    public SwitchBugemonSubView() throws IOException {
        super("/fxml/fight/switchbugemonview.fxml");
    }

    @Override
    protected String getTitle() {
        return "Changer de Bugemon";
    }

    public void displayTeam(List<BugemonDTO> team, boolean showCancel, Object currentOnFieldId) {
        grid.getChildren().clear();

        for (int i = 0; i < team.size(); i++) {
            BugemonDTO bugemon = team.get(i);
            ImageView imgView = createSprite(bugemon);
            bindEvents(imgView, bugemon, currentOnFieldId);
            grid.add(imgView, i % 3, i / 3);
        }

        btnCancel.setVisible(showCancel);
        btnCancel.setManaged(showCancel);

        if (showCancel) {
            btnCancel.setOnAction(e -> controller.onCancelClicked());
            int row = (team.size() - 1) / 3 + 1;
            grid.add(btnCancel, 1, row);
            GridPane.setHalignment(btnCancel, HPos.CENTER);
        }
    }

    private ImageView createSprite(BugemonDTO bugemon) {
        Image     img  = new Image(Objects.requireNonNull(
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

    private void bindEvents(ImageView imgView, BugemonDTO bugemon, Object currentOnFieldId) {
        imgView.setOnMouseEntered(e -> showHpPopup(e, bugemon, imgView));
        imgView.setOnMouseMoved(this::updatePopupPosition);
        imgView.setOnMouseExited(e -> popup.hideHp());
        imgView.setOnMouseClicked(e -> controller.onBugemonClicked(bugemon));
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