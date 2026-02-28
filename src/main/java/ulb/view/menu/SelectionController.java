package ulb.view.menu;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import ulb.models.Bugemon.Bugemon;

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

    String currentBugemons;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //List<String> bugemonsList = loadBugemonNames();
        //myListView.getItems().addAll(bugemonsList);

        myListView.setCellFactory(lv -> new BugemonListCell());

        loadData();

        myListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                myLabel.setText(newVal.getNom());
            }
        });
    }

    private void loadData() {
        File folder = new File("src/main/resources/Assets/png");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null){
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".png")) {
                    String name = file.getName().replace(".png", "");
                    Bugemon b = new Bugemon(name);
                    myListView.getItems().add(b);
                }
            }
        }
    }
/*
        myListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {

                currentBugemons = myListView.getSelectionModel().getSelectedItem();

                myLabel.setText(currentBugemons);
            }
        });
    }
*/
/*
    private List<String> loadBugemonNames() {
        List<String> names = new ArrayList<>();

        File folder = new File("src/main/resources/Assets/png");

        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().endsWith(".png")) {
                    String nameWithoutExtension = file.getName().replace(".png", "");
                    names.add(nameWithoutExtension);
                }
            }
        } else {
            System.err.println("Dossier non trouvé : " + folder.getAbsolutePath());
        }
        return names;
    }
*/
}
