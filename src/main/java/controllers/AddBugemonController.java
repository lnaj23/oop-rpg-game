package controllers;

import exceptions.JsonResourceNotFoundException;
import models.bugemon.Attack;
import services.dataprovider.AttackProvider;
import views.AddBugemonView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.io.File;

public class AddBugemonController extends Controller<AddBugemonView> {

    private final AttackProvider attackProvider = new AttackProvider();
    private final List<Attack> attacksToShow;

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

    private String checkSpritePath(String spritePath) {
        if (spritePath == null || spritePath.isBlank()) {
            return "Le sprite est obligatoire.";
        }

        File spriteFile = new File(spritePath);

        if (!spriteFile.exists()) {
            return "Le fichier du sprite n'existe pas.";
        }

        if (!spriteFile.isFile()) {
            return "Le chemin du sprite n'est pas un fichier valide.";
        }

        String lowerPath = spriteFile.getName().toLowerCase();
        if (!(lowerPath.endsWith(".png") || lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg"))) {
            return "Le sprite doit être une image PNG, JPG ou JPEG.";
        }

        try {
            BufferedImage image = ImageIO.read(spriteFile);
            if (image == null) {
                return "Le fichier sélectionné n'est pas une image valide.";
            }
        } catch (IOException e) {
            return "Impossible de lire le fichier image du sprite.";
        }

        return null;
    }

    private String checkAttack(Attack attack1, Attack attack2, Attack attack3) {
        if (attack1 == null || attack2 == null || attack3 == null) {
            return "Il faut sélectionner 3 attaques.";
        }

        if (attack1.equals(attack2) || attack1.equals(attack3) || attack2.equals(attack3)) {
            return "Les 3 attaques doivent être différentes.";
        }

        return null;
    }

    private String checkStats(String hpText, String attackText, String defenseText) {
        int hp;
        int attack;
        int defense;

        try {
            hp = Integer.parseInt(hpText);
            attack = Integer.parseInt(attackText);
            defense = Integer.parseInt(defenseText);
        } catch (NumberFormatException e) {
            return "HP, Attaque et Défense doivent être des nombres.";
        }

        if (hp <= 0 || attack < 0 || defense < 0) {
            return "Les statistiques sont invalides.";
        }

        return null;
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
        String errorMessage;

        if (name == null || name.isBlank()) {
            view.showMessage("Le nom est obligatoire.");
            return;
        }

        if (type == null) {
            view.showMessage("Le type est obligatoire.");
            return;
        }

        errorMessage = checkStats(hpText, attackText, defenseText);
        if (errorMessage != null) {
            view.showMessage(errorMessage);
            return;
        }

        errorMessage = checkAttack(attack1, attack2, attack3);
        if (errorMessage != null) {
            view.showMessage(errorMessage);
            return;
        }

        errorMessage = checkSpritePath(spritePath);
        if (errorMessage != null) {
            view.showMessage(errorMessage);
            return;
        }

        if (starterText == null) {
            view.showMessage("Il faut choisir si le Bugémon est starter.");
            return;
        }

        int hp = Integer.parseInt(hpText);
        int attack = Integer.parseInt(attackText);
        int defense = Integer.parseInt(defenseText);

        boolean starter = "Oui".equals(starterText);


        // A supprimer par la suite
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
