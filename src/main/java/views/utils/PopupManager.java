package views.utils;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;
import models.bugemon.dto.BugemonDTO;

/**
 * Encapsulates the two standard popups used across views:
 * an HP popup (on hover) and a stats popup (on click).
 * <p>
 * Designed for composition: any class — whether it extends {@link views.View}
 * or not (e.g. sub-controllers loaded via {@code fx:include}) — can hold
 * an instance and delegate all popup operations to it.
 * </p>
 */
public class PopupManager {

    private final Popup hpPopup    = new Popup();
    private final Label hpLabel    = new Label();
    private final Popup statsPopup = new Popup();
    private final Label statsLabel = new Label();

    private static final String TOOLTIP_STYLE =
            "-fx-background-color: rgba(0,0,0,0.8); " +
                    "-fx-text-fill: white; " +
                    "-fx-padding: 5; " +
                    "-fx-background-radius: 5;";

    /** Initialises both popups. */
    public PopupManager() {
        initPopup(hpPopup,    hpLabel);
        initPopup(statsPopup, statsLabel);
    }


    /**
     * Shows the HP popup next to the cursor.
     *
     * @param e      mouse event carrying screen coordinates
     * @param b      the Bugémon whose HP to display
     * @param anchor the node used as owner for the popup
     */
    public void showHpPopup(MouseEvent e, BugemonDTO b, Node anchor) {
        hpLabel.setText("HP : " + b.getcurrenPv() + " / " + b.getPv());
        hpPopup.show(anchor, e.getScreenX() + 15, e.getScreenY() + 15);
    }

    /**
     * Moves the HP popup to follow the cursor.
     *
     * @param e the latest mouse-moved event
     */
    public void updateHpPosition(MouseEvent e) {
        hpPopup.setX(e.getScreenX() + 15);
        hpPopup.setY(e.getScreenY() + 15);
    }

    /** Hides the HP popup. */
    public void hideHp() { hpPopup.hide(); }


    /**
     * Shows the stats popup at the given screen position.
     *
     * @param x      screen X of the triggering event
     * @param y      screen Y of the triggering event
     * @param b      the Bugémon whose stats to display
     * @param anchor the node used as owner for the popup
     */
    public void showStatsPopup(double x, double y, BugemonDTO b, Node anchor) {
        statsLabel.setText(
                (b.getIsPlayer() ? "Niveau " + b.getlevel() + "\n" : "" ) +
                "PV: "      + b.getPv()                 + "\n" +
                        "ATK: "     + b.getAttaque()             + "\n" +
                        "DEF: "     + b.getDefense()             + "\n" +
                        "INIT: "    + b.getInitiative()          + "\n" +
                        "Attaques: "+ b.getAttackNamesAsString()
        );
        statsPopup.show(anchor, x + 15, y + 15);
    }

    /**
     * Moves the stats popup to follow the cursor.
     *
     * @param e the latest mouse-moved event
     */
    public void updateStatsPosition(MouseEvent e) {
        statsPopup.setX(e.getScreenX() + 15);
        statsPopup.setY(e.getScreenY() + 15);
    }

    /** Hides the stats popup. */
    public void hideStats() { statsPopup.hide(); }

    /** @return {@code true} if the stats popup is currently visible */
    public boolean isStatsShowing() { return statsPopup.isShowing(); }


    /** @return the underlying HP {@link Popup} */
    public Popup getHpPopup()    { return hpPopup; }

    /** @return the underlying stats {@link Popup} */
    public Popup getStatsPopup() { return statsPopup; }


    private void initPopup(Popup popup, Label label) {
        label.setMouseTransparent(true);
        label.setStyle(TOOLTIP_STYLE);
        popup.getContent().add(label);
        popup.setAutoHide(true);
    }
}