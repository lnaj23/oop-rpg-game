/**
 * Plain (non-{@link javafx.application.Application}) entry point.
 * <p>
 * Exists so the application can be launched from a runnable JAR without the
 * JavaFX runtime being on the module path: it simply delegates to
 * {@link Main#main(String[])}.
 * </p>
 */
public class Launcher {

    public static void main(String[] args) {
        Main.main(args);
    }
}
