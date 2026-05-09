package services.controller;

import exceptions.LoadingAddedBugemonFailedException;
import exceptions.SavingAddedBugemonFailedException;
import models.bugemon.Attack;
import models.bugemon.Bugemon;
import repositories.CustomBugemonRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Service responsible for validating the data entered in the Add Bugemon form.
 * Returns a structured {@link AddBugemonError} instead of a UI string —
 * the view layer is responsible for formatting.
 */
public class AddBugemonService {

    /**
     * Validates all the data required to create a new Bugemon.
     *
     * @return an {@link AddBugemonError} if validation fails, or {@code null} if all data is valid
     */
    public AddBugemonError validate(
            String name,
            String type,
            String hpText,
            String attackText,
            String defenseText,
            String initiativeText,
            String spritePath,
            Attack attack1,
            Attack attack2,
            Attack attack3,
            String starterText
    ) {
        if (name == null || name.isBlank()) {
            return AddBugemonError.NAME_REQUIRED;
        }

        if (type == null || type.isBlank()) {
            return AddBugemonError.TYPE_REQUIRED;
        }

        AddBugemonError error = checkStats(hpText, attackText, defenseText, initiativeText);
        if (error != null) {
            return error;
        }

        error = checkAttack(attack1, attack2, attack3);
        if (error != null) {
            return error;
        }

        error = checkSpritePath(spritePath);
        if (error != null) {
            return error;
        }

        if (starterText == null || starterText.isBlank()) {
            return AddBugemonError.STARTER_REQUIRED;
        }

        return null;
    }

    private AddBugemonError checkSpritePath(String spritePath) {
        if (spritePath == null || spritePath.isBlank()) {
            return AddBugemonError.SPRITE_REQUIRED;
        }

        File spriteFile = new File(spritePath);

        if (!spriteFile.exists()) {
            return AddBugemonError.SPRITE_FILE_NOT_FOUND;
        }

        if (!spriteFile.isFile()) {
            return AddBugemonError.SPRITE_NOT_A_FILE;
        }

        String lowerPath = spriteFile.getName().toLowerCase();
        if (!(lowerPath.endsWith(".png") || lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg"))) {
            return AddBugemonError.SPRITE_WRONG_FORMAT;
        }

        try {
            BufferedImage image = ImageIO.read(spriteFile);
            if (image == null) {
                return AddBugemonError.SPRITE_NOT_VALID_IMAGE;
            }
        } catch (IOException e) {
            return AddBugemonError.SPRITE_NOT_VALID_IMAGE;
        }

        return null;
    }

    private AddBugemonError checkAttack(Attack attack1, Attack attack2, Attack attack3) {
        if (attack1 == null || attack2 == null || attack3 == null) {
            return AddBugemonError.ATTACK_COUNT_INVALID;
        }

        if (attack1.equals(attack2) || attack1.equals(attack3) || attack2.equals(attack3)) {
            return AddBugemonError.ATTACKS_NOT_DISTINCT;
        }

        return null;
    }

    private AddBugemonError checkStats(String hpText, String attackText, String defenseText, String initiativeText) {
        int hp;
        int attack;
        int defense;
        int initiative;

        try {
            hp = Integer.parseInt(hpText);
            attack = Integer.parseInt(attackText);
            defense = Integer.parseInt(defenseText);
            initiative = Integer.parseInt(initiativeText);
        } catch (NumberFormatException e) {
            return AddBugemonError.STATS_NOT_NUMERIC;
        }

        if (hp <= 0 || attack < 0 || defense < 0 || initiative < 0) {
            return AddBugemonError.STATS_INVALID;
        }

        return null;
    }

    public void saveBugemon(Bugemon bugemon, File selectedSprite, boolean flipHorizontally) throws SavingAddedBugemonFailedException, LoadingAddedBugemonFailedException, IOException {
        CustomBugemonRepository.getInstance().saveAddedBugemons(bugemon);
        CustomBugemonRepository.getInstance().saveSprite(selectedSprite, flipHorizontally);
    }
}