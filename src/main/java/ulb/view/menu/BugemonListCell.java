package ulb.view.menu;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import ulb.models.Bugemon.Bugemon;

public class BugemonListCell extends ListCell<Bugemon> {
    private final HBox content;
    private final ImageView imageView;
    private final Label label;

    public BugemonListCell() {
        super();
        content = new HBox(15);
        content.setAlignment(Pos.CENTER_LEFT);

        imageView = new ImageView();
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        imageView.setPreserveRatio(true);

        label = new Label();
        content.getChildren().addAll(imageView, label);
    }

    @Override
    protected void updateItem(Bugemon bugemon, boolean empty) {
        super.updateItem(bugemon, empty);

        if (empty || bugemon == null) {
            setGraphic(null);
            setText(null);
        } else {
            try {
                String imagePath = "/Assets/png/" + bugemon.getNom() + ".png";
                Image img = new Image(getClass().getResourceAsStream(imagePath));
                imageView.setImage(img);
            } catch (Exception e) {
                imageView.setImage(null);
            }

            label.setText(bugemon.getNom());
            setGraphic(content);
        }
    }
}
