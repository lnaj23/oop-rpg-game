package services.game;

import models.bugemon.Attack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Service responsible for validating the data entered in the Add Bugemon form.
 * This service checks whether the required fields are filled in correctly,
 * verifies that the stats are valid numeric values, ensures that the selected
 * attacks are valid and distinct, and confirms that the sprite path points to
 * a readable image file.
 */
public class AddBugemonValidationService {

    /**
     * Validates all the data required to create a new Bugemon.
     *
     * @param name the Bugemon name
     * @param type the Bugemon type
     * @param hpText the HP value entered as text
     * @param attackText the attack stat entered as text
     * @param defenseText the defense stat entered as text
     * @param spritePath the file path of the selected sprite
     * @param attack1 the first selected attack
     * @param attack2 the second selected attack
     * @param attack3 the third selected attack
     * @param starterText the selected starter value
     * @return an error message if validation fails, or {@code null} if all data is valid
     */
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

    /**
     * Checks whether the provided sprite path points to a valid readable image file.
     *
     * @param spritePath the path of the sprite file
     * @return an error message if the sprite is invalid, or {@code null} if it is valid
     */
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

    /**
     * Checks whether the selected attacks are all present and distinct.
     *
     * @param attack1 the first selected attack
     * @param attack2 the second selected attack
     * @param attack3 the third selected attack
     * @return an error message if the attacks are invalid, or {@code null} if they are valid
     */
    private String checkAttack(Attack attack1, Attack attack2, Attack attack3) {
        if (attack1 == null || attack2 == null || attack3 == null) {
            return "Il faut sélectionner 3 attaques.";
        }

        if (attack1.equals(attack2) || attack1.equals(attack3) || attack2.equals(attack3)) {
            return "Les 3 attaques doivent être différentes.";
        }

        return null;
    }

    /**
     * Checks whether the stat fields contain valid numeric values.
     *
     * @param hpText the HP value entered as text
     * @param attackText the attack stat entered as text
     * @param defenseText the defense stat entered as text
     * @return an error message if the stats are invalid, or {@code null} if they are valid
     */
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