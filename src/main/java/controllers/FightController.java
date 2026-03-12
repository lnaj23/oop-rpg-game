package controllers;

import views.FightView;
import java.io.IOException;

public class FightController extends Controller<FightView> {

    public FightController(MetaController metaController) throws IOException {
        super(metaController, new FightView());
        this.view.setController(this);
    }

    public void goToTitleScreen() {
        this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN);
    }

    public void onAttackSelected(int attackIndex) {
        // choisir attack
    }

    public void onItemClicked() {
        // choisir objet
    }

    public void onSwitchClicked() {
        // changer de Bugémon
    }

    public void onForfeitClicked() {
        this.metaController.switchScene(MetaController.Scenes.TITLE_SCREEN);
    }
}
