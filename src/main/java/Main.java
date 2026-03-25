import controllers.MetaController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        MetaController meta = new MetaController(primaryStage);
        meta.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}