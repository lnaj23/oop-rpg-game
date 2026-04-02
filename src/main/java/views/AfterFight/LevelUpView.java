package views.AfterFight;

import configuration.AppConfig;
import controllers.AfterFight.LevelUpController;
import views.View;

import java.io.IOException;

public class LevelUpView extends View<LevelUpController> {

    public LevelUpView() throws IOException {
        super(AppConfig.FXML_LEVELUP);
    }

    @Override
    protected String getTitle() {
        return "Level Up";
    }
}
