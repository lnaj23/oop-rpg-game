package repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exceptions.JsonPersistenceLoadingException;
import exceptions.JsonPersistenceSavingException;
import exceptions.JsonResourceNotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base repository providing generic JSON serialization utilities.
 * <p>
 * Three operations are exposed:
 * <ul>
 *   <li>{@link #readJSONFromAssets} — reads read-only data packaged on the classpath
 *       (game data such as Bugémons, attacks, items).</li>
 *   <li>{@link #readPersistenceData} — reads mutable user data from a filesystem path
 *       (saved teams, custom Bugémons).</li>
 *   <li>{@link #updatePersistenceData} — writes mutable user data to a filesystem path.</li>
 * </ul>
 * </p>
 * <p>
 * Subclasses must define an inner wrapper class implementing {@link JsonWrapper}
 * to use any of the read methods.
 * </p>
 */
public abstract class JsonRepository {

    /** Used for both filesystem writes; kept private so subclasses don't share this instance. */
    private static final Gson SAVE_GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Contract imposed on every wrapper class used for JSON deserialization.
     *
     * @param <T> the type of the elements contained in the list
     */
    protected interface JsonWrapper<T> {
        /** @return the list extracted from the deserialized wrapper */
        List<T> getJsonData();
    }

    /**
     * Reads a JSON file from the classpath, deserializes it into the given wrapper class,
     * and returns the list exposed by that wrapper via {@link JsonWrapper#getJsonData()}.
     *
     * @param path         the classpath-relative path to the JSON file
     * @param wrapperClass the wrapper class mirroring the JSON structure
     * @throws JsonResourceNotFoundException if the file is not found in the classpath
     */
    protected static <W extends JsonWrapper<T>, T> List<T> readJSONFromAssets(String path, Class<W> wrapperClass) throws JsonResourceNotFoundException {
        Gson gson = new Gson();
        InputStream stream = JsonRepository.class.getResourceAsStream(path);
        if (stream == null) throw new JsonResourceNotFoundException(path);
        JsonWrapper<T> wrapper = gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), wrapperClass);
        return wrapper.getJsonData();
    }

    /**
     * Reads a JSON file from the filesystem and returns the list it contains.
     * Returns an empty list if the file does not exist.
     *
     * @param path         the filesystem path to the JSON file
     * @param wrapperClass the wrapper class mirroring the JSON structure
     * @throws JsonPersistenceLoadingException on I/O error or malformed JSON
     */
    protected static <W extends JsonWrapper<T>, T> List<T> readPersistenceData(String path, Class<W> wrapperClass) throws JsonPersistenceLoadingException {
        File file = new File(path);
        if (!file.exists()) return new ArrayList<>();
        try (Reader reader = new FileReader(file)) {
            JsonWrapper<T> wrapper = new Gson().fromJson(reader, wrapperClass);
            if (wrapper == null || wrapper.getJsonData() == null) return new ArrayList<>();
            return wrapper.getJsonData();
        } catch (IOException e) {
            throw new JsonPersistenceLoadingException("Error loading from " + path + ": " + e.getMessage(), e);
        }
    }

    /**
     * Serializes the given wrapper to a JSON file on the filesystem, creating
     * any missing parent directories.
     *
     * @param path    the filesystem path where the JSON file will be written
     * @param wrapper a populated wrapper holding the data to serialize
     * @throws JsonPersistenceSavingException on I/O error
     */
    protected static void updatePersistenceData(String path, JsonWrapper<?> wrapper) throws JsonPersistenceSavingException {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null) parent.mkdirs();
        try (Writer writer = new FileWriter(file)) {
            SAVE_GSON.toJson(wrapper, writer);
        } catch (IOException e) {
            throw new JsonPersistenceSavingException("Error saving to " + path + ": " + e.getMessage(), e);
        }
    }
}