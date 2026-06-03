1. Expliquez comment gérer les différences de niveaux entre les développeurs en méthodologie XP.

2. `GameStateHolder.getInstance()` stocke l'état de la session. Expliquez le pattern Singleton. Quel est le problème avec ce point de votre architecture ? Comment pourrait-on améliorer cela ?
    - Proposez une refactorisation pour injecter `GameStateHolder` via une interface `IGameStateHolder` et un conteneur de dépendances simple.

3. `BugemonDTO` est une interface avec uniquement des getters. Pourquoi exposer une interface plutôt que la classe `Bugemon` directement à la vue ? Quelle règle SOLID justifie ce choix ? Pourrait-on utiliser un autre mécanisme en Java pour garantir l'immuabilité ? Quels sont les avantages et les inconvénients de chaque approche ?


4. `JsonRepository` distingue `readJSONFromAssets()` (classpath) et `readPersistenceData()` (filesystem). Expliquez la différence technique. Pourquoi les données de jeu sont-elles dans le classpath alors que les données utilisateur sont sur le filesystem ? Ensuite, il est indiqué "Subclasses must define an inner wrapper class implementing {@link JsonWrapper} to use any of the read methods.". Quelle technique auriez vous pu utiliser pour forcer ça ?

```java
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
 * Subclasses must define an inner wrapper class implementing {@link JsonWrapper}
 * to use any of the read methods.
 */
public abstract class JsonRepository {

    private static final Gson SAVE_GSON = new GsonBuilder().setPrettyPrinting().create();

    protected interface JsonWrapper<T> {
        /** @return the list extracted from the deserialized wrapper */
        List<T> getJsonData();
    }

    protected static <W extends JsonWrapper<T>, T> List<T> readJSONFromAssets(String path, Class<W> wrapperClass) throws JsonResourceNotFoundException {
        Gson gson = new Gson();
        InputStream stream = JsonRepository.class.getResourceAsStream(path);
        if (stream == null) throw new JsonResourceNotFoundException(path);
        JsonWrapper<T> wrapper = gson.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), wrapperClass);
        return wrapper.getJsonData();
    }

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
```

5. `TypeEffectivenessService` résout le calcul des dommages par types avec une approche KISS. Expliquez pourquoi c'est une approche KISS. Quelle serait une alternative plus "orientée objet" ?

```java
    public static double getTypeMultiplier(String attackType, String targetType) {
        if (attackType == null || targetType == null) return 1.0;

        String combo = attackType.toLowerCase() + "_" + targetType.toLowerCase();
        return switch (combo) {
            case "flora_aqua", "aqua_pyro", "pyro_litho", "litho_flora" -> 1.5;
            case "aqua_flora", "pyro_aqua", "litho_pyro", "flora_litho" -> 0.75;
            default -> 1.0;
        };
    }
```

6. Le projet définit `TowerNotInitializedException`, `BugemonNotInTeamException`, etc. Mais vous utilisez aussi quelques `IllegalStateException`. Quelle est la différence entre ces deux exceptions ? Sachant que vos exceptions héritent de `Exception` et que `IllegalStateException` est une `RuntimeException`, expliquez les implications pour le code qui les utilise.


7. `DamageCalculator` est une classe avec uniquement des méthodes statiques. Expliquez les avantages et inconvénients de ce design. A quoi sert la méthode statique `setRandom()` ?

```java
public abstract class DamageCalculator {

    private static Random random = new Random();

    public static void setRandom(Random r) {
        random = r;
    }

    public static int computeDamage(Bugemon attacker, Bugemon target, Attack attack) {

        double attackFactor = (100.0 + attacker.getAttaque()) / 100.0;

        double reductionFactor = 100.0 / (100.0 + target.getDefense());

        double damage = attack.getPower() * attackFactor * reductionFactor;

        double typeMultiplier = TypeEffectivenessService.getTypeMultiplier(
                attack.getType(),
                target.getType()
        );
        damage *= typeMultiplier;

        boolean isCritical = random.nextInt(100) < 10;
        if (isCritical) damage *= 1.5;

        return (int) damage;
    }
}
```

8. `EffectManager` utilise `Optional`. Expliquez ce que représente `Optional<T>` en Java et pourquoi c'est une bonne pratique de l'utiliser dans ce cas.

```java
    private static Optional<EffectEvent> handleSingleEffect(Bugemon attacker, Bugemon target, TeamBugemon attackerTeam, Effect effet) {
        return switch (effet.getType()) {
            case "soin" -> handleHealEffect(attacker, attackerTeam, effet);
            case "stat_modifier" -> handleStatModifierEffect(attacker, target, effet);
            default -> Optional.empty();
        };
    }
```


9. Expliquez ce que fait la ligne suivante `public sealed interface EffectEvent permits HealSelfEvent, HealTeamEvent, StatModifierEvent {}`. Cette interface est une interface vide. Quel est l'intérêt de cette approche ?

10. Vous utilisez un `Consumer` dans la classe `FightAnimator`. Définissez ce concept et expliquez son utilité dans ce contexte.

```java
public class FightAnimator {

    private final ImageView    playerSprite;
    private final ImageView    enemySprite;
    private final ProgressBar  playerHpBar;
    private final ProgressBar  enemyHpBar;

    private final Consumer<BugemonDTO> hideCallback;
    
    // ...

    public void animateKO(BugemonDTO bugemon, Runnable afterKOAnimation) {
        ImageView sprite = bugemon.isPlayer() ? playerSprite : enemySprite;

        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), sprite);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> {
            if (hideCallback != null) hideCallback.accept(bugemon);
            if (afterKOAnimation != null) afterKOAnimation.run();
        });
        fade.play();
    }

}