package services.view;


import services.controller.AddBugemonError;

/**
 * Formats an {@link AddBugemonError} into the user-facing error message.
 */
public abstract class AddBugemonErrorFormatter {

    public static String format(AddBugemonError error) {
        return switch (error) {
            case NAME_REQUIRED          -> "Le nom est obligatoire.";
            case TYPE_REQUIRED          -> "Le type est obligatoire.";
            case STATS_NOT_NUMERIC      -> "HP, Attaque, Défense et Initiative doivent être des nombres.";
            case STATS_INVALID          -> "Les statistiques sont invalides.";
            case ATTACK_COUNT_INVALID   -> "Il faut sélectionner 3 attaques.";
            case ATTACKS_NOT_DISTINCT   -> "Les 3 attaques doivent être différentes.";
            case SPRITE_REQUIRED        -> "Le sprite est obligatoire.";
            case SPRITE_NOT_SELECTED    -> "Il faut choisir un sprite avec le bouton Parcourir.";
            case SPRITE_FILE_NOT_FOUND  -> "Le fichier du sprite n'existe pas.";
            case SPRITE_NOT_A_FILE      -> "Le chemin du sprite n'est pas un fichier valide.";
            case SPRITE_WRONG_FORMAT    -> "Le sprite doit être une image PNG, JPG ou JPEG.";
            case SPRITE_NOT_VALID_IMAGE -> "Le fichier sélectionné n'est pas une image valide.";
            case STARTER_REQUIRED       -> "Il faut choisir si le Bugémon est starter.";
        };
    }
}
