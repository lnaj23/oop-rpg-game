package repositories;

import configuration.AppConfig;
import exceptions.JsonPersistenceLoadingException;
import exceptions.JsonPersistenceSavingException;
import exceptions.LoadingAddedBugemonFailedException;
import exceptions.SavingAddedBugemonFailedException;
import models.bugemon.Bugemon;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Singleton repository for user-created Bugémons.
 * <p>
 * Persists their JSON definitions and copies their sprite PNGs into the
 * configured persistence directory.
 * </p>
 */
public class CustomBugemonRepository extends JsonRepository {

    private static CustomBugemonRepository instance;

    /**
     * JSON wrapper mirroring the {addedBugemons: [...]} structure on disk.
     * The field name is preserved for backward compatibility with existing save files.
     */
    private static class JsonCustomBugemons implements JsonWrapper<Bugemon> {
        List<Bugemon> addedBugemons;
        @Override
        public List<Bugemon> getItems() { return addedBugemons; }
    }

    private CustomBugemonRepository() {}

    public static CustomBugemonRepository getInstance() {
        if (instance == null) instance = new CustomBugemonRepository();
        return instance;
    }

    public List<Bugemon> loadAddedBugemon() throws LoadingAddedBugemonFailedException {
        try {
            return readPersistenceData(AppConfig.ADDEDBUGEMON_FILE, JsonCustomBugemons.class);
        } catch (JsonPersistenceLoadingException e) {
            throw new LoadingAddedBugemonFailedException("Error loading Added Bugemon", e);
        }
    }

    /**
     * Appends the given Bugémon to the existing list and persists the new list.
     */
    public void saveAddedBugemons(Bugemon bugemonToSave) throws LoadingAddedBugemonFailedException, SavingAddedBugemonFailedException {
        List<Bugemon> existing = loadAddedBugemon();
        existing.add(bugemonToSave);
        JsonCustomBugemons wrapper = new JsonCustomBugemons();
        wrapper.addedBugemons = existing;
        try {
            updatePersistenceData(AppConfig.ADDEDBUGEMON_FILE, wrapper);
        } catch (JsonPersistenceSavingException e) {
            throw new SavingAddedBugemonFailedException("Error saving Added Bugemon", e);
        }
    }

    /** Copies the given image file into the configured sprite directory. */
    public void saveSprite(File selectedFile) throws IOException {
        Path source = selectedFile.toPath();
        Path destination = Path.of(AppConfig.PNG_PREFIX_ADDEDBUEMON, selectedFile.getName());
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }
}