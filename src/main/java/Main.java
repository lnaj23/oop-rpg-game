import controllers.ControllerManager;
import javafx.application.Application;
import javafx.stage.Stage;
import views.FatalErrorView;


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