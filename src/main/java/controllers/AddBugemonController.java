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

    AttackProvider attackProvider = new AttackProvider();
    List<Attack> attacksToShow;

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

        if (name == null || name.isBlank()) {
            view.showMessage("Le nom est obligatoire.");
            return;
        }

        if (type == null) {
            view.showMessage("Le type est obligatoire.");
            return;
        }

        if (spritePath == null || spritePath.isBlank()) {
            view.showMessage("Le sprite est obligatoire.");
            return;
        }

        File spriteFile = new File(spritePath);

        if (!spriteFile.exists()) {
            view.showMessage("Le fichier du sprite n'existe pas.");
            return;
        }

        if (!spriteFile.isFile()) {
            view.showMessage("Le chemin du sprite n'est pas un fichier valide.");
            return;
        }

        String lowerPath = spriteFile.getName().toLowerCase();
        if (!(lowerPath.endsWith(".png") || lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg"))) {
            view.showMessage("Le sprite doit être une image PNG, JPG ou JPEG.");
            return;
        }

        try {
            BufferedImage image = ImageIO.read(spriteFile);
            if (image == null) {
                view.showMessage("Le fichier sélectionné n'est pas une image valide.");
                return;
            }
        } catch (IOException e) {
            view.showMessage("Impossible de lire le fichier image du sprite.");
            return;
        }

        if (attack1 == null || attack2 == null || attack3 == null) {
            view.showMessage("Il faut sélectionner 3 attaques.");
            return;
        }

        if (starterText == null) {
            view.showMessage("Il faut choisir si le Bugémon est starter.");
            return;
        }

        int hp;
        int attack;
        int defense;

        try {
            hp = Integer.parseInt(hpText);
            attack = Integer.parseInt(attackText);
            defense = Integer.parseInt(defenseText);
        } catch (NumberFormatException e) {
            view.showMessage("HP, Attaque et Défense doivent être des nombres.");
            return;
        }

        if (hp <= 0 || attack < 0 || defense < 0) {
            view.showMessage("Les statistiques sont invalides.");
            return;
        }

        boolean starter = starterText.equals("Oui");

        if (attack1.equals(attack2) || attack1.equals(attack3) || attack2.equals(attack3)) {
            view.showMessage("Les 3 attaques doivent être différentes.");
            return;
        }

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

        // ajout de l'appel au service pour sajid
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
