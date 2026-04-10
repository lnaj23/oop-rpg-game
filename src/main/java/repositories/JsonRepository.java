package repositories;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Abstract base repository providing a generic JSON deserialization utility.
 * <p>
 * Subclasses must define an inner wrapper class implementing {@link JsonWrapper}
 * in order to use {@link #readJSONFromAssets}.
 * </p>
 */
public abstract class JsonRepository {

    /**
     * Contract imposed on every wrapper class used for JSON deserialization.
     * <p>
     * Each wrapper must expose its list via {@link #getItems()}, allowing
     * {@link #readJSONFromAssets} to extract the content without an external extractor.
     * </p>
     *
     * @param <T> the type of the elements contained in the list
     */
    protected interface JsonWrapper<T> {
        /**
         * Returns the list extracted from the deserialized wrapper.
         *
         * @return the list of elements
         */
        List<T> getItems();
    }

    /**
     * Reads a JSON file from the classpath, deserializes it into the given wrapper class,
     * and returns the list exposed by that wrapper via {@link JsonWrapper#getItems()}.
     *
     * @param path         the classpath-relative path to the JSON file
     * @param wrapperClass the wrapper class mirroring the JSON structure,
     *                     must implement {@link JsonWrapper}
     * @param <W>          the wrapper type
     * @param <T>          the element type
     * @return the list extracted from the JSON file
     * @throws IllegalStateException if the file is not found in the classpath
     */
    protected static <W extends JsonWrapper<T>, T> List<T> readJSONFromAssets(String path, Class<W> wrapperClass) {
        Gson gson = new Gson();
        InputStream stream = JsonRepository.class.getResourceAsStream(path);
        if (stream == null) throw new IllegalStateException("Resource not found: " + path);
        JsonWrapper<T> wrapper = gson.fromJson(new InputStreamReader(stream), wrapperClass);
        return wrapper.getItems();
    }
}