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
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.bugemon.dto.BugemonDTO;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Modal window for selecting the next on-field Bugémon.
 * Hover shows current HP, left-click selects.
 */
public class SwitchBugemonView {

    @FXML private GridPane grid;
    @FXML private Button   btnCancel;
    @FXML private Label    titleLabel;

    private final Stage stage;

    /** Popup shown on hover, displaying current HP. */
    private final Popup hpPopup = new Popup();
    /** Label inside {@link #hpPopup}. */
    private final Label hpLabel = new Label();

    /**
     * Constructs the switch window.
     *
     * @param team              the player's team to display
     * @param onPurpose        {@code true} if the player opened the window voluntarily
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
        initHpPopup();
        populateGrid(team, currentOnFieldId, onBugemonSelected);
        if (onPurpose) initCancelButton(team.size(), onCancel);
    }

    private boolean loadFxml(boolean showCancel) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/fight/switchbugemonview.fxml")
            );
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

    private void initHpPopup() {
        hpLabel.setMouseTransparent(true);
        hpLabel.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-text-fill: white; " +
                "-fx-padding: 5; -fx-background-radius: 5;");
        hpPopup.getContent().add(hpLabel);
        hpPopup.setAutoHide(true);
    }

    private void populateGrid(List<BugemonDTO> team, Object currentOnFieldId,
                              Consumer<Object> onBugemonSelected) {
        for (int i = 0; i < team.size(); i++) {
            BugemonDTO bugemon = team.get(i);
            ImageView imgView = createBugemonSprite(bugemon);
            bindBugemonEvents(imgView, bugemon, currentOnFieldId, onBugemonSelected);
            grid.add(imgView, i % 3, i / 3);
        }
    }

    private ImageView createBugemonSprite(BugemonDTO bugemon) {
        String path = "/Assets/png/" + bugemon.getSprite();
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(80);
        imgView.setFitWidth(80);
        imgView.setPreserveRatio(true);
        if (bugemon.getcurrenPv() <= 0) {
            imgView.setEffect(new ColorAdjust(0, -1, 0, 0));
        }
        return imgView;
    }

    private void bindBugemonEvents(ImageView imgView, BugemonDTO bugemon,
                                   Object currentOnFieldId, Consumer<Object> onBugemonSelected) {
        imgView.setOnMouseEntered(e -> {
            hpLabel.setText("HP : " + bugemon.getcurrenPv() + " / " + bugemon.getPv());
            hpPopup.show(imgView, e.getScreenX() + 15, e.getScreenY() + 15);
        });
        imgView.setOnMouseMoved(e -> {
            hpPopup.setX(e.getScreenX() + 15);
            hpPopup.setY(e.getScreenY() + 15);
        });
        imgView.setOnMouseExited(e -> hpPopup.hide());
        imgView.setOnMouseClicked(e -> handleBugemonClick(bugemon, currentOnFieldId, onBugemonSelected));
    }

    private void handleBugemonClick(BugemonDTO bugemon, Object currentOnFieldId,
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
        btnCancel.setOnAction(e -> {
            stage.close();
            onCancel.run();
        });
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