package services.game;

import models.bugemon.Attack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AddBugemonValidationService {

    public String validate(
            String name,
            String type,
            String hpText,
            String attackText,
            String defenseText,
            String spritePath,
            Attack attack1,
            Attack attack2,
            Attack attack3,
            String starterText
    ) {
        if (name == null || name.isBlank()) {
            return "Le nom est obligatoire.";
        }

        if (type == null || type.isBlank()) {
            return "Le type est obligatoire.";
        }

        String errorMessage = checkStats(hpText, attackText, defenseText);
        if (errorMessage != null) {
            return errorMessage;
        }

        errorMessage = checkAttack(attack1, attack2, attack3);
        if (errorMessage != null) {
            return errorMessage;
        }

        errorMessage = checkSpritePath(spritePath);
        if (errorMessage != null) {
            return errorMessage;
        }

        if (starterText == null || starterText.isBlank()) {
            return "Il faut choisir si le Bugémon est starter.";
        }

        return null;
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
}