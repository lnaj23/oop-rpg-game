package views.fight;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.bugemon.dto.BugemonDTO;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class SwitchBugemonView {

    private final Stage stage;
    private final GridPane grid;

    public SwitchBugemonView(List<BugemonDTO> team, Consumer<Object> onBugemonSelected) {
        this.stage = new Stage();
        this.grid = new GridPane();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Choisir un Bugémon");

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setStyle("-fx-padding: 15; -fx-alignment: center;");

        for (int i = 0; i < team.size(); i++) {
            BugemonDTO bugemon = team.get(i);

            String path = "/Assets/png/" + bugemon.getSprite();
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));

            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(60);
            imgView.setFitWidth(60);
            imgView.setPreserveRatio(true);

            if (bugemon.getcurrenPv() <= 0) {
                imgView.setEffect(new ColorAdjust(0, -1, 0, 0));
            }

            imgView.setOnMouseClicked(e -> {
                if (bugemon.getcurrenPv() <= 0) {
                    showMessageKO("Erreur", "Ce Bugémon est KO.");
                } else {
                    stage.close();
                    onBugemonSelected.accept(bugemon.getId());
                }
            });

            grid.add(imgView, i % 3, i / 3);
        }

        Scene scene = new Scene(grid, 260, 200);
        stage.setScene(scene);
    }
    public void showMessageKO(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void show() {
        stage.show();
    }
}