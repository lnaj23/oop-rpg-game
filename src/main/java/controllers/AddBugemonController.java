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

/**
 * Controller responsible for the Add Bugemon screen.
 * <p>
 * This controller initializes the view, loads the available attacks,
 * handles user interactions such as choosing a sprite file, validates
 * the form input through the validation service, and triggers the
 * creation flow for a new Bugemon.
 * </p>
 */
public class AddBugemonController extends Controller<AddBugemonView> {

    /** Provides access to the available attacks loaded from the game data. */
    private final AttackProvider attackProvider = new AttackProvider();

    /** List of attacks displayed in the choice boxes of the form. */
    private final List<Attack> attacksToShow;

    /** Service used to validate the values entered in the Add Bugemon form. */
    private final AddBugemonValidationService validationService = new AddBugemonValidationService();

    /**
     * Creates the controller and initializes the associated Add Bugemon view.
     *
     * @param metaController the main controller manager of the application
     * @throws IOException if the FXML view cannot be loaded
     * @throws JsonResourceNotFoundException if the attack data resource cannot be found
     */
    public AddBugemonController(ControllerManager metaController) throws IOException, JsonResourceNotFoundException {
        super(metaController, new AddBugemonView());
        this.view.setController(this);
        this.view.setActionButtons();
        attacksToShow = attackProvider.getAllAttacks();
    }

    /**
     * Initializes the Add Bugemon view by populating its choice boxes
     * with the available types, attacks, and starter options.
     */
    public void init() {
        this.view.setTypeChoiceBox();
        this.view.setAttackChoiceBox(attacksToShow);
        this.view.setStarterChoiceBox();
    }

    /**
     * Validates the values entered in the form and, if valid,
     * displays a success message summarizing the created Bugemon.
     * <p>
     * The actual creation and persistence logic is intended to be added later
     * through a dedicated service.
     * </p>
     */
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

    /**
     * Opens a file chooser to let the user select a sprite image file.
     * <p>
     * Only PNG files are proposed in the chooser. If a file is selected,
     * its absolute path is written into the sprite path field of the view.
     * </p>
     */
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

    /**
     * Navigates back to the title screen.
     */
    public void goToTitleScreen() {
        this.controllerManager.goToTitleScreen();
    }
}