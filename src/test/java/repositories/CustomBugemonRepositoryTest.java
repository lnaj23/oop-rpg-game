package repositories;

import configuration.AppConfig;
import exceptions.JsonResourceNotFoundException;
import exceptions.LoadingAddedBugemonFailedException;
import exceptions.SavingAddedBugemonFailedException;
import models.bugemon.Attack;
import models.bugemon.Bugemon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class CustomBugemonRepositoryTest {

    private final CustomBugemonRepository repo = CustomBugemonRepository.getInstance();

    /** Sprite reused from the {@code nobugemon} folder (the one used for remaining enemy bugémons). */
    private static final File SPRITE_SOURCE = new File("src/main/resources/Assets/png/nobugemon/ulb.png");

    /** Where {@code saveSprite()} is expected to copy the PNG. */
    private static final Path SPRITE_DESTINATION = Path.of(AppConfig.PNG_PREFIX_ADDEDBUEMON, SPRITE_SOURCE.getName());

    /** In-memory backup of the real persistence files, taken before each test. */
    private byte[] jsonBackup;
    private byte[] spriteBackup;

    @Before
    public void backupAndCleanup() throws IOException {
        // 1) Back up the real files if they already exist
        File jsonFile = new File(AppConfig.ADDEDBUGEMON_FILE);
        jsonBackup   = jsonFile.exists()                ? Files.readAllBytes(jsonFile.toPath()) : null;
        spriteBackup = Files.exists(SPRITE_DESTINATION) ? Files.readAllBytes(SPRITE_DESTINATION) : null;

        // 2) Start the test from a clean state
        if (jsonFile.exists()) jsonFile.delete();
        Files.deleteIfExists(SPRITE_DESTINATION);
    }

    @Test
    public void testSaveAndLoadCustomBugemonWithSprite()
            throws JsonResourceNotFoundException, LoadingAddedBugemonFailedException,
            SavingAddedBugemonFailedException, IOException {

        // 3 arbitrary attacks to fill the required fields
        List<Attack> attacks = AttackRepository.getInstance().getAllAttacks().subList(0, 3);
        List<String> attackNames = List.of(
                attacks.get(0).getId(),
                attacks.get(1).getId(),
                attacks.get(2).getId()
        );

        // Build the test Bugémon (same constructor as in AddBugemonController)
        Bugemon original = new Bugemon(
                "test",
                "Pyro",
                50,
                10,
                10,
                10,
                false,
                SPRITE_SOURCE.getName(),
                attacks,
                attackNames
        );

        // Persist: JSON + sprite copy
        repo.saveAddedBugemons(original);
        repo.saveSprite(SPRITE_SOURCE);

        // Reload and verify the Bugémon
        List<Bugemon> loaded = repo.loadAddedBugemon();
        assertEquals("Should contain exactly 1 saved Bugémon", 1, loaded.size());

        Bugemon reloaded = loaded.get(0);
        assertEquals("test", reloaded.getName());
        assertEquals("Pyro", reloaded.getType());
        assertEquals(50, reloaded.getPv());
        assertFalse("A custom Bugémon must not be marked as default", reloaded.isDefault());
        assertEquals(SPRITE_SOURCE.getName(), reloaded.getSprite());
        assertEquals(3, reloaded.getAttackNamesAsList().size());

        // Verify the sprite was actually copied to the destination
        assertTrue("Sprite should be copied to the persistence directory",
                Files.exists(SPRITE_DESTINATION));
    }

    @After
    public void restore() throws IOException {
        File jsonFile = new File(AppConfig.ADDEDBUGEMON_FILE);
        if (jsonFile.exists()) jsonFile.delete();
        Files.deleteIfExists(SPRITE_DESTINATION);

        if (jsonBackup != null) {
            Files.write(jsonFile.toPath(), jsonBackup);
        }
        if (spriteBackup != null) {
            Files.write(SPRITE_DESTINATION, spriteBackup);
        }
    }
}