package controllers;

import views.MenuView;

import java.io.IOException;

public class MenuController extends Controller<MenuView> {

    public MenuController(MetaController metaController) throws IOException {
        super(metaController, new MenuView());
        this.view.setController(this);
    }

    public void goToSelection() {
        this.metaController.openNewSelection();
    }

    public void goToPreBuildTeam() {
        this.metaController.switchScene(MetaController.Scenes.PREBUILD_TEAM);
    }
}