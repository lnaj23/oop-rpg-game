/*
package ulb.view;

import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.api.FxAssert;
import org.testfx.matcher.control.LabeledMatchers;
import ulb.view.menu.MenuView;

import java.io.IOException;


public class TestSceneController extends ApplicationTest {
    @Override
    public void start(Stage stage) throws IOException {
        new MenuView().start(stage);
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

 */