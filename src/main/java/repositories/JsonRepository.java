package repositories;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;

/**
 * Abstract base repository providing a generic JSON deserialization utility.
 * Subclasses use {@link #readJSON} to load their respective data files.
 */
public abstract class JsonRepository {

    /** Base path prefix for all JSON resource files. */
    protected static final String JSON_PREFIX = "/Assets/json/";

    /**
     * Reads a JSON file from the classpath, deserializes it into the given wrapper class,
     * and extracts the list using the provided extractor function.
     *
     * @param path          the classpath-relative path to the JSON file
     * @param wrapperClass  the class mirroring the JSON structure
     * @param extractor     function to extract the list from the deserialized wrapper
     * @param <W>           the wrapper type
     * @param <T>           the element type
     * @return the extracted list
     * @throws IllegalStateException if the file is not found
     */
    protected static <W, T> List<T> readJSON(String path, Class<W> wrapperClass, Function<W, List<T>> extractor) {
        Gson gson = new Gson();
        InputStream stream = JsonRepository.class.getResourceAsStream(path);

        if (stream == null) throw new IllegalStateException("Resource not found: " + path);

        return extractor.apply(gson.fromJson(new InputStreamReader(stream), wrapperClass));
    }
}