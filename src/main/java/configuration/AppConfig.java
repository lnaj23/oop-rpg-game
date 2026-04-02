package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Centralizes all paths and configuration constants of the game.
 * Loaded only once from app.properties at startup.
 */
public abstract class AppConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = AppConfig.class.getResourceAsStream("/app.properties")) {
            if (in == null) throw new IllegalStateException("app.properties not found in classpath");
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load app.properties", e);
        }
    }

    // JSON
    public static final String JSON_BUGEMONS  = props.getProperty("json.prefix") + props.getProperty("json.bugemons");
    public static final String JSON_ATTAQUES  = props.getProperty("json.prefix") + props.getProperty("json.attaques");

    // Persistence
    public static final String TEAMS_FILE     = props.getProperty("persistence.teams.file");

    // FXML
    public static final String FXML_TITLESCREEN   = props.getProperty("fxml.titlescreen");
    public static final String FXML_FIGHT         = props.getProperty("fxml.fight");
    public static final String FXML_ITEMS         = props.getProperty("fxml.items");
    public static final String FXML_SWITCHBUGEMON = props.getProperty("fxml.switchbugemon");
    public static final String FXML_PREBUILDTEAM  = props.getProperty("fxml.prebuildteam");
    public static final String FXML_SELECTION     = props.getProperty("fxml.selection");
    public static final String FXML_LEVELUP       = props.getProperty("fxml.levelup");

    // Assets PNG
    public static final String PNG_PREFIX     = props.getProperty("assets.png.prefix");
    public static final String PNG_NOBUGEMON  = PNG_PREFIX + props.getProperty("assets.png.nobugemon");

    // CSS
    public static final String CSS_COMMON     = props.getProperty("css.common");

    // Game constant
    public static final int    MAX_TEAM_SIZE         = Integer.parseInt(props.getProperty("game.team.max_size"));
    public static final int    MIN_TEAM_SIZE         = Integer.parseInt(props.getProperty("game.team.min_size"));
    public static final int    ENEMY_REACT_TIME      = Integer.parseInt(props.getProperty("game.fight.enemy_react_time"));
    public static final double DEATH_DISPLAY_TIME    = Double.parseDouble(props.getProperty("game.fight.death_display_time"));

    public static List<String> getEnemyTeam() {
        return Arrays.asList(props.getProperty("game.fight.enemy_team").split(","));
    }
}