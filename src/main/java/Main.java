import controllers.MetaController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        MetaController meta = new MetaController(stage);
        meta.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}