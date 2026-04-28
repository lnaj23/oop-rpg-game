package views.fight;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.bugemon.dto.BugemonDTO;

import java.util.function.Consumer;

/**
 * Handles all JavaFX animations for the fight screen.
 * <p>
 * Extracted from {@link FightView} to respect the Single Responsibility Principle.
 * Responsible for: attack lunge, hit flash + HP bar decrease, and KO fade-out.
 * </p>
 * <p>
 * The {@code hideCallback} is invoked by {@link #animateKO} to hide the
 * fainted Bugémon's UI elements (sprite, HP bar, labels) after the fade completes.
 * It is provided by {@link FightView} via a method reference to its own
 * {@code handleVisibility} helper.
 * </p>
 */
public class FightAnimator {

    private final ImageView    playerSprite;
    private final ImageView    enemySprite;
    private final ProgressBar  playerHpBar;
    private final ProgressBar  enemyHpBar;

    /**
     * Called by {@link #animateKO} once the fade-out is complete,
     * so that {@link FightView} can hide the relevant UI nodes.
     */
    private final Consumer<BugemonDTO> hideCallback;

    /**
     * Constructs a {@code FightAnimator} bound to the given UI nodes.
     *
     * @param playerSprite  the player's active Bugémon sprite
     * @param enemySprite   the enemy's active Bugémon sprite
     * @param playerHpBar   the player's HP progress bar
     * @param enemyHpBar    the enemy's HP progress bar
     * @param hideCallback  called with the fainted Bugémon to hide its UI elements;
     *                      typically {@code bugemon -> handleVisibility(bugemon, false)}
     */
    public FightAnimator(ImageView playerSprite, ImageView enemySprite,
                         ProgressBar playerHpBar,  ProgressBar enemyHpBar,
                         Consumer<BugemonDTO> hideCallback) {
        this.playerSprite = playerSprite;
        this.enemySprite  = enemySprite;
        this.playerHpBar  = playerHpBar;
        this.enemyHpBar   = enemyHpBar;
        this.hideCallback = hideCallback;
    }

    /**
     * Plays a forward-lunge then recoil animation for the attacking Bugémon.
     * <p>
     * Player lunges right (+60 px), enemy lunges left (−60 px).
     * The sprite is snapped back to {@code translateX = 0} before the callback fires.
     * </p>
     *
     * @param bugemon              the attacking Bugémon (used to pick the right sprite)
     * @param afterAttackAnimation optional callback run after the animation completes
     */
    public void animateAttack(BugemonDTO bugemon, Runnable afterAttackAnimation) {
        ImageView sprite    = bugemon.getIsPlayer() ? playerSprite : enemySprite;
        double    direction = bugemon.getIsPlayer() ? 60 : -60;

        TranslateTransition forward = new TranslateTransition(Duration.seconds(0.3), sprite);
        forward.setByX(direction);

        TranslateTransition back = new TranslateTransition(Duration.seconds(0.2), sprite);
        back.setByX(-direction);

        SequentialTransition seq = new SequentialTransition(forward, back);
        seq.setOnFinished(e -> {
            // Guarantee exact position before handing off
            sprite.setTranslateX(0);
            if (afterAttackAnimation != null) afterAttackAnimation.run();
        });
        seq.play();
    }

    /**
     * Plays a red-flash effect on the hit Bugémon while simultaneously
     * animating the HP bar down to its new value.
     * <p>
     * The DTO must already reflect the new HP before this method is called.
     * Both the flash and the HP timeline run in parallel; the callback fires when both finish.
     * </p>
     *
     * @param bugemon            the Bugémon that was hit (DTO already updated with new HP)
     * @param afterHitAnimation  optional callback run after the animation completes
     */
    public void animateHit(BugemonDTO bugemon, Runnable afterHitAnimation) {
        ImageView   sprite = bugemon.getIsPlayer() ? playerSprite : enemySprite;
        ProgressBar hpBar  = bugemon.getIsPlayer() ? playerHpBar  : enemyHpBar;

        double targetProgress;
        try {
            targetProgress = Math.round(
                    (bugemon.getcurrenPv() / (double) bugemon.getPv()) * 10000d) / 10000d;
        } catch (Exception e) {
            targetProgress = hpBar.getProgress();
        }

        KeyValue   kv         = new KeyValue(hpBar.progressProperty(), targetProgress, Interpolator.EASE_BOTH);
        KeyFrame   kf         = new KeyFrame(Duration.seconds(0.3), kv);
        Timeline   hpTimeline = new Timeline(kf);

        // Bounds must be read on the JavaFX thread after layout
        Platform.runLater(() -> {
            Bounds b = sprite.getBoundsInLocal();
            double w = b.getWidth()  > 0 ? b.getWidth()  : (sprite.getFitWidth()  > 0 ? sprite.getFitWidth()  : 100);
            double h = b.getHeight() > 0 ? b.getHeight() : (sprite.getFitHeight() > 0 ? sprite.getFitHeight() : 100);

            ColorInput redInput = new ColorInput(0, 0, w, h, Color.rgb(255, 0, 0, 0.6));
            Blend      redTint  = new Blend(BlendMode.SRC_ATOP, null, redInput);

            Timeline flash = new Timeline(
                    new KeyFrame(Duration.seconds(0.00), e -> sprite.setEffect(redTint)),
                    new KeyFrame(Duration.seconds(0.05), e -> sprite.setEffect(null)),
                    new KeyFrame(Duration.seconds(0.10), e -> sprite.setEffect(redTint)),
                    new KeyFrame(Duration.seconds(0.15), e -> sprite.setEffect(null)),
                    new KeyFrame(Duration.seconds(0.20), e -> sprite.setEffect(redTint)),
                    new KeyFrame(Duration.seconds(0.25), e -> sprite.setEffect(null)),
                    new KeyFrame(Duration.seconds(0.30), e -> sprite.setEffect(null))
            );

            ParallelTransition parallel = new ParallelTransition(flash, hpTimeline);
            parallel.setOnFinished(e -> {
                sprite.setEffect(null);
                if (afterHitAnimation != null) afterHitAnimation.run();
            });
            parallel.play();
        });
    }

    /**
     * Plays a fade-out animation on the KO'd Bugémon's sprite,
     * then hides its UI elements via the {@link #hideCallback} before running the callback.
     *
     * @param bugemon           the KO'd Bugémon
     * @param afterKOAnimation  optional callback run after the animation and hide complete
     */
    public void animateKO(BugemonDTO bugemon, Runnable afterKOAnimation) {
        ImageView sprite = bugemon.getIsPlayer() ? playerSprite : enemySprite;

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
