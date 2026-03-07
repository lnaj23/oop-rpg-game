package controllers;

import javafx.stage.Stage;
import views.View;

public abstract class Controller<T extends View> {

    protected final MetaController metaController;
    protected final T view;

    public Controller(MetaController metaController, T view) {
        this.metaController = metaController;
        this.view = view;
    }

    public void show(Stage stage) {
        this.view.show(stage);
    }
}