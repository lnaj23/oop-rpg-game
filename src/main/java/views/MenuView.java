package views;

import controllers.MenuController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class MenuView extends View {

    private MenuController controller;

    @FXML private Label titelLabel;
    @FXML private Button btnNewGame;
    @FXML private Button btnPreBuildTeam;

    public MenuView() throws IOException {
        super("/fxml/menuview.fxml");
        this.btnNewGame.setOnAction(e -> this.controller.goToSelection());
        this.btnPreBuildTeam.setOnAction(e -> this.controller.goToPreBuildTeam());
    }

    public void setController(MenuController controller) {
        this.controller = controller;
    }

    @Override
    protected String getTitle() {
        return "Bugémon";
    }
}