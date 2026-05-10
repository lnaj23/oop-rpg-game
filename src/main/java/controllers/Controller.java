package controllers;

import javafx.stage.Stage;
import views.View;

/**
 * Abstract base controller that links a {@link ControllerManager} to a {@link View}.
 *
 * @param <T> the type of view managed by this controller.
 */
public abstract class Controller<T extends View> {

    /** The application's central controller, used for navigation. */
    protected final ControllerManager controllerManager;

    /** The view associated with this controller. */
    protected final T view;

    /**
     * @param metaController the central controller handling navigation
     * @param view the view managed by this controller
     */
    public Controller(ControllerManager metaController, T view) {
        this.controllerManager = metaController;
        this.view = view;
    }

    /**
     * Displays the associated view on the given stage.
     *
     * @param stage the JavaFX stage to display the view on
     */
    public void show(Stage stage) {
        this.view.show(stage);
    }

    public void attachTo(Stage stage) {
        view.attachTo(stage);
    }

}