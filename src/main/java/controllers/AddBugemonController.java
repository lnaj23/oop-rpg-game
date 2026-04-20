package controllers;

import exceptions.JsonResourceNotFoundException;
import models.bugemon.Attack;
import services.dataprovider.AttackProvider;
import services.game.AddBugemonValidationService;
import views.AddBugemonView;

import java.io.IOException;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class AddBugemonController extends Controller<AddBugemonView> {

    private final AttackProvider attackProvider = new AttackProvider();
    private final List<Attack> attacksToShow;
    private final AddBugemonValidationService validationService = new AddBugemonValidationService();

    public AddBugemonController(ControllerManager metaController) throws IOException, JsonResourceNotFoundException {
        super(metaController, new AddBugemonView());
        this.view.setController(this);
        this.view.setActionButtons();
        attacksToShow = attackProvider.getAllAttacks();
    }

    public void init() {
        this.view.setTypeChoiceBox();
        this.view.setAttackChoiceBox(attacksToShow);
        this.view.setStarterChoiceBox();
    }

    public void addBugemon() {
        String name = view.getNameValue();
        String hpText = view.getHpValue();
        String attackText = view.getAttackValue();
        String defenseText = view.getDefenseValue();
        String spritePath = view.getSpritePathValue();

        String type = view.getSelectedType();
        Attack attack1 = view.getSelectedAttack1();
        Attack attack2 = view.getSelectedAttack2();
        Attack attack3 = view.getSelectedAttack3();
        String starterText = view.getSelectedStarter();

        String errorMessage = validationService.validate(
                name,
                type,
                hpText,
                attackText,
                defenseText,
                spritePath,
                attack1,
                attack2,
                attack3,
                starterText
        );

        if (errorMessage != null) {
            view.showMessage(errorMessage);
            return;
        }

        int hp = Integer.parseInt(hpText);
        int attack = Integer.parseInt(attackText);
        int defense = Integer.parseInt(defenseText);
        boolean starter = "Oui".equals(starterText);

        view.showMessage(
                "Bugémon ajouté avec succès !\n\n" +
                        "Nom : " + name + "\n" +
                        "Type : " + type + "\n" +
                        "HP : " + hp + "\n" +
                        "Attaque : " + attack + "\n" +
                        "Défense : " + defense + "\n" +
                        "Sprite : " + spritePath + "\n" +
                        "Attaque 1 : " + attack1.getName() + "\n" +
                        "Attaque 2 : " + attack2.getName() + "\n" +
                        "Attaque 3 : " + attack3.getName() + "\n" +
                        "Starter : " + starter
        );

        // ajout de l'appel au service pour Sajid
    }

    public void chooseSprite() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un sprite");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images PNG", "*.png")
        );

        Window window = view.getBtnChooseSprite().getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(window);

        if (selectedFile != null) {
            view.setSpritePath(selectedFile.getAbsolutePath());
        }
    }

    /** Navigates back to the main menu. */
    public void goToTitleScreen() {
        this.controllerManager.goToTitleScreen();
    }

}
