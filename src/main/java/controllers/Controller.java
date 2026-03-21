package controllers;

import javafx.stage.Stage;
import views.View;

/**
 * Abstract base controller that links a {@link MetaController} to a {@link View}.
 *
 * @param <T> the type of view managed by this controller.
 */
public abstract class Controller<T extends View> {

    /** The application's central controller, used for navigation. */
    protected final MetaController metaController;

    /** The view associated with this controller. */
    protected final T view;

    /**
     * @param metaController the central controller handling navigation
     * @param view the view managed by this controller
     */
    public Controller(MetaController metaController, T view) {
        this.metaController = metaController;
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

}