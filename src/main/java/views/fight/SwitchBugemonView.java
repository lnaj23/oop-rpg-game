package views.fight;

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
import views.util.PopupManager;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Modal window for selecting the next on-field Bugémon.
 * Hover shows current HP via {@link PopupManager}, left-click selects.
 */
public class SwitchBugemonView {

    @FXML private GridPane grid;
    @FXML private Button   btnCancel;
    @FXML private Label    titleLabel;

    private final Stage        stage;
    private final PopupManager popup = new PopupManager();

    /**
     * Constructs the switch window.
     *
     * @param team              the player's team to display
     * @param onPurpose         {@code true} if the player opened the window voluntarily
     * @param currentOnFieldId  id of the Bugémon currently on the field
     * @param onBugemonSelected callback triggered with the selected Bugémon's id
     * @param onCancel          callback triggered when the player cancels (no turn cost)
     */
    public SwitchBugemonView(List<BugemonDTO> team, boolean onPurpose,
                             Object currentOnFieldId,
                             Consumer<Object> onBugemonSelected, Runnable onCancel) {
        this.stage = new Stage();
        if (!loadFxml(onPurpose)) return;
        initStage();
        populateGrid(team, currentOnFieldId, onBugemonSelected);
        if (onPurpose) initCancelButton(team.size(), onCancel);
    }


    private boolean loadFxml(boolean showCancel) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/fight/switchbugemonview.fxml"));
            loader.setController(this);
            Scene scene = new Scene(loader.load(), 320, showCancel ? 310 : 260);
            stage.setScene(scene);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void initStage() {
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
    }


    private void populateGrid(List<BugemonDTO> team, Object currentOnFieldId,
                              Consumer<Object> onBugemonSelected) {
        for (int i = 0; i < team.size(); i++) {
            BugemonDTO bugemon = team.get(i);
            ImageView  imgView = createSprite(bugemon);
            bindEvents(imgView, bugemon, currentOnFieldId, onBugemonSelected);
            grid.add(imgView, i % 3, i / 3);
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

    private void bindEvents(ImageView imgView, BugemonDTO bugemon,
                            Object currentOnFieldId, Consumer<Object> onBugemonSelected) {
        imgView.setOnMouseEntered(e -> popup.showHpPopup(e, bugemon, imgView));
        imgView.setOnMouseMoved(popup::updateHpPosition);
        imgView.setOnMouseExited(e -> popup.hideHp());
        imgView.setOnMouseClicked(e -> handleClick(bugemon, currentOnFieldId, onBugemonSelected));
    }

    private void handleClick(BugemonDTO bugemon, Object currentOnFieldId,
                             Consumer<Object> onBugemonSelected) {
        if (bugemon.getcurrenPv() <= 0) {
            showMessageKO("Erreur", "Ce Bugémon est KO.");
        } else if (bugemon.getId().equals(currentOnFieldId)) {
            showMessageKO("Erreur", "Ce Bugémon est déjà sur le terrain !");
        } else {
            stage.close();
            onBugemonSelected.accept(bugemon.getId());
        }
    }

    private void initCancelButton(int teamSize, Runnable onCancel) {
        btnCancel.setVisible(true);
        btnCancel.setManaged(true);
        btnCancel.setOnAction(e -> { stage.close(); onCancel.run(); });
        int row = (teamSize - 1) / 3 + 1;
        grid.add(btnCancel, 1, row);
        GridPane.setHalignment(btnCancel, HPos.CENTER);
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

    /** Displays the modal window. */
    public void show() {
        stage.show();
    }
}