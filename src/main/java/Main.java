import controllers.ControllerManager;
import javafx.application.Application;
import javafx.stage.Stage;
import views.FatalErrorView;


/**
 * JavaFX application entry point.
 * <p>
 * Bootstraps the {@link ControllerManager} on the primary stage and starts the
 * application. Any failure during startup is treated as fatal: a
 * {@link FatalErrorView} is shown and the process exits.
 * </p>
 */
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