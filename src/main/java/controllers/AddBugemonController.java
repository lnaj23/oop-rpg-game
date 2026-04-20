package controllers;

import exceptions.JsonResourceNotFoundException;
import models.bugemon.Attack;
import services.dataprovider.AttackProvider;
import views.AddBugemonView;

import java.io.IOException;
import java.util.List;

public class AddBugemonController extends Controller<AddBugemonView> {

    AttackProvider attackProvider = new AttackProvider();
    List<Attack> attacksToShow;

    public AddBugemonController(ControllerManager metaController) throws IOException, JsonResourceNotFoundException {
        super(metaController, new AddBugemonView());
        this.view.setController(this);
        this.view.setActionButtons();
        attacksToShow = attackProvider.getAllAttacks();
    }

    public void init() {
        this.view.setAttackChoiceBox(attacksToShow);
    }

    public void addBugemon(){

    }

    /** Navigates back to the main menu. */
    public void goToTitleScreen() {
        this.controllerManager.goToTitleScreen();
    }
}
