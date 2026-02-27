package ulb;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.team.PreBuildTeamView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        PreBuildTeamView view = new PreBuildTeamView();
        stage.setScene(new Scene(view, 400, 500));
        stage.setTitle("Prebuilt Teams");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}