package services.controller.addbugemon;

import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import junit.framework.TestCase;
import models.bugemon.Attack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.controller.dataprovider.AttackProvider;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AddBugemonServiceTest extends TestCase {

    private AddBugemonService service;
    private File validSprite;
    private File wrongFormatFile;
    private File emptyPngFile;
    private File notImageFile;
    private Attack a1, a2, a3;

    @Before
    public void setUp() throws IOException, AttackNotFoundException, JsonResourceNotFoundException {
        service = new AddBugemonService();
        AttackProvider attackProvider = new AttackProvider();
        a1 = attackProvider.getAttackById("fouet_liane");
        a2 = attackProvider.getAttackById("feuille_tranchante");
        a3 = attackProvider.getAttackById("explosion_ardente");

        validSprite = File.createTempFile("sprite_valid_", ".png");
        ImageIO.write(new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB), "png", validSprite);

        wrongFormatFile = File.createTempFile("sprite_", ".txt");
        Files.writeString(wrongFormatFile.toPath(), "not an image");

        emptyPngFile = File.createTempFile("sprite_empty_", ".png");

        notImageFile = Files.createTempDirectory("not_a_file_").toFile();
    }

    @After
    public void tearDown() {
        if (validSprite != null) validSprite.delete();
        if (wrongFormatFile != null) wrongFormatFile.delete();
        if (emptyPngFile != null) emptyPngFile.delete();
        if (notImageFile != null) notImageFile.delete();
    }

    @Test
    public void testValidateReturnsNameRequiredWhenNameNull() {
        AddBugemonError err = service.validate(null, "Flora", "100", "50", "50", "50",
                validSprite.getAbsolutePath(), a1, a2, a3, "true");
        assertEquals(AddBugemonError.NAME_REQUIRED, err);
    }

    @Test
    public void testValidateReturnsNameRequiredWhenNameBlank() {
        AddBugemonError err = service.validate("   ", "Flora", "100", "50", "50", "50",
                validSprite.getAbsolutePath(), a1, a2, a3, "true");
        assertEquals(AddBugemonError.NAME_REQUIRED, err);
    }

    @Test
    public void testValidateReturnsTypeRequiredWhenTypeBlank() {
        AddBugemonError err = service.validate("toto", "", "100", "50", "50", "50",
                validSprite.getAbsolutePath(), a1, a2, a3, "true");
        assertEquals(AddBugemonError.TYPE_REQUIRED, err);
    }

    @Test
    public void testValidateReturnsStatsNotNumericForNonInteger() {
        AddBugemonError err = service.validate("toto", "Flora", "abc", "50", "50", "50",
                validSprite.getAbsolutePath(), a1, a2, a3, "true");
        assertEquals(AddBugemonError.STATS_NOT_NUMERIC, err);
    }

    @Test
    public void testValidateReturnsStatsInvalidForZeroHp() {
        AddBugemonError err = service.validate("toto", "Flora", "0", "50", "50", "50",
                validSprite.getAbsolutePath(), a1, a2, a3, "true");
        assertEquals(AddBugemonError.STATS_INVALID, err);
    }

    @Test
    public void testValidateReturnsStatsInvalidForNegativeStat() {
        AddBugemonError err = service.validate("toto", "Flora", "100", "-1", "50", "50",
                validSprite.getAbsolutePath(), a1, a2, a3, "true");
        assertEquals(AddBugemonError.STATS_INVALID, err);
    }

    @Test
    public void testValidateReturnsAttackCountInvalidWhenAttackNull() {
        AddBugemonError err = service.validate("toto", "Flora", "100", "50", "50", "50",
                validSprite.getAbsolutePath(), a1, null, a3, "true");
        assertEquals(AddBugemonError.ATTACK_COUNT_INVALID, err);
    }

    @Test
    public void testValidateReturnsAttacksNotDistinctForDuplicates() {
        AddBugemonError err = service.validate("toto", "Flora", "100", "50", "50", "50",
                validSprite.getAbsolutePath(), a1, a1, a3, "true");
        assertEquals(AddBugemonError.ATTACKS_NOT_DISTINCT, err);
    }

    @Test
    public void testValidateReturnsSpriteRequiredForBlankPath() {
        AddBugemonError err = service.validate("toto", "Flora", "100", "50", "50", "50",
                "", a1, a2, a3, "true");
        assertEquals(AddBugemonError.SPRITE_REQUIRED, err);
    }

    @Test
    public void testValidateReturnsSpriteFileNotFoundForNonExisting() {
        AddBugemonError err = service.validate("toto", "Flora", "100", "50", "50", "50",
                "/tmp/nope_does_not_exist_xyz.png", a1, a2, a3, "true");
        assertEquals(AddBugemonError.SPRITE_FILE_NOT_FOUND, err);
    }

    @Test
    public void testValidateReturnsSpriteNotAFileForDirectory() {
        AddBugemonError err = service.validate("toto", "Flora", "100", "50", "50", "50",
                notImageFile.getAbsolutePath(), a1, a2, a3, "true");
        assertEquals(AddBugemonError.SPRITE_NOT_A_FILE, err);
    }

    @Test
    public void testValidateReturnsSpriteWrongFormatForTxt() {
        AddBugemonError err = service.validate("toto", "Flora", "100", "50", "50", "50",
                wrongFormatFile.getAbsolutePath(), a1, a2, a3, "true");
        assertEquals(AddBugemonError.SPRITE_WRONG_FORMAT, err);
    }

    @Test
    public void testValidateReturnsSpriteNotValidImageForCorruptedPng() {
        AddBugemonError err = service.validate("toto", "Flora", "100", "50", "50", "50",
                emptyPngFile.getAbsolutePath(), a1, a2, a3, "true");
        assertEquals(AddBugemonError.SPRITE_NOT_VALID_IMAGE, err);
    }

    @Test
    public void testValidateReturnsStarterRequiredWhenStarterBlank() {
        AddBugemonError err = service.validate("toto", "Flora", "100", "50", "50", "50",
                validSprite.getAbsolutePath(), a1, a2, a3, "");
        assertEquals(AddBugemonError.STARTER_REQUIRED, err);
    }

    @Test
    public void testValidateReturnsNullForValidInput() {
        AddBugemonError err = service.validate("toto", "Flora", "100", "50", "50", "50",
                validSprite.getAbsolutePath(), a1, a2, a3, "true");
        assertNull("expected no error", err);
    }
}