package services.view;

import configuration.AppConfig;
import javafx.scene.image.Image;
import models.bugemon.dto.BugemonDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Utility class for loading Bugémon sprites.
 * Default sprites are loaded from the classpath (resources),
 * while added Bugémon sprites are loaded from the filesystem.
 */
public class SpriteLoader {
    public static Image loadSprite(BugemonDTO bugemon) throws IOException {
        if (bugemon.isDefault()) {
            String path = AppConfig.PNG_PREFIX + bugemon.getSprite();
            return new Image(Objects.requireNonNull(SpriteLoader.class.getResourceAsStream(path)));
        }
        return new Image(new FileInputStream(Path.of(AppConfig.PNG_PREFIX_ADDEDBUEMON, bugemon.getSprite()).toString()));
    }
}