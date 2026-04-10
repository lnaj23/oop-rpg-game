import controllers.ControllerManager;
import exceptions.LoadingTeamFailedException;
import javafx.application.Application;
import javafx.stage.Stage;
import services.game.GameStateHolder;
import views.FatalErrorView;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            ControllerManager meta = new ControllerManager(primaryStage);
            meta.start();
        } catch (Exception e) {
            FatalErrorView.show("Impossible de charger l'application.");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}