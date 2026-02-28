package ulb.view;

import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest; // Import crucial
import org.testfx.api.FxAssert;                 // Import crucial
import org.testfx.matcher.control.LabeledMatchers; // Import crucial
import ulb.view.menu.MenuView;

import java.io.IOException;


public class TestSceneController extends ApplicationTest {
    @Override
    public void start(Stage stage) throws IOException {
        new MenuView().start(stage);
    }
    static {
        if ("true".equals(System.getenv("CI"))) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
    }
    @Test
    public void shouldGoToSelectionView(){
        FxAssert.verifyThat("#titelLabel", LabeledMatchers.hasText("BUGÉMON"));
        clickOn("#btnNewGame");
        FxAssert.verifyThat("#subtitleLabel", LabeledMatchers.hasText("Sélection d'équipe"));
    }

    @Test
    public void shouldGoBackToMenuView(){
        clickOn("#btnNewGame");
        clickOn("#btnReturn");
        FxAssert.verifyThat("#titelLabel", LabeledMatchers.hasText("BUGÉMON"));
    }
}