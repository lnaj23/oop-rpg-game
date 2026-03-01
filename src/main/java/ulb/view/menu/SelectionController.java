package ulb.view.menu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ulb.models.Bugemon.Bugemon;
import ulb.models.Bugemon.BugemonProvider;
import ulb.models.Bugemon.TeamBugemon;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SelectionController implements Initializable {
    public Button btnReturn;
    public Label subtitleLabel;

    public void switchToMenuView(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("menuview.fxml"));
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private ListView<Bugemon> myListView;

    @FXML
    private Label myLabel;

    private TeamBugemon playerTeam = new TeamBugemon();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        myListView.getItems().addAll(BugemonProvider.getAllBugemons());

        myListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateTeam();
        });
        myListView.setCellFactory(lv -> new ListCell<Bugemon>() {
            private final ImageView imageView = new ImageView();
            @Override
            protected void updateItem(Bugemon bugemon, boolean empty) {
                super.updateItem(bugemon, empty);

                if (empty || bugemon == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String path = "/Assets/png/" + bugemon.getSprite();

                    try {

                        Image img = new Image(getClass().getResourceAsStream(path));

                        imageView.setImage(img);
                        imageView.setFitHeight(40);
                        imageView.setPreserveRatio(true);

                        setText(bugemon.getNom());
                        setGraphic(imageView);

                    } catch (Exception e) {
                        setText(bugemon.getNom() + " (Image manquante)");
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void updateTeam() {
        List<Bugemon> selectedBugemons = myListView.getSelectionModel().getSelectedItems();

        playerTeam = new TeamBugemon();

        if (selectedBugemons.size() <= 6) {
            for (Bugemon bug : selectedBugemons) {
                playerTeam.addBugemon(bug);
            }
            myLabel.setText("Équipe : " + playerTeam.getSize() + " / 6 Bugémons");
        } else {
            myLabel.setText("Trop de Bugémons ! (Max 6)");
        }
    }

    @FXML
    public void confirmSelection() {
        if (playerTeam.getSize() >= 1 && playerTeam.getSize() <= 6) {
            System.out.println("Équipe validée : ");
            playerTeam.printTeam();
        }
    }
}
