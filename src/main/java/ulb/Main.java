package ulb;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.controller.team.PreBuildTeamController;
import ulb.models.team.PreBuildTeamService;
import ulb.view.team.PreBuildTeamView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        PreBuildTeamView view = new PreBuildTeamView();
        PreBuildTeamService service = new PreBuildTeamService();
        PreBuildTeamController controller = new PreBuildTeamController(view, service);
        controller.init();

        stage.setScene(new Scene(view, 400, 500));
        stage.setTitle("Choisissez une équipe");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}