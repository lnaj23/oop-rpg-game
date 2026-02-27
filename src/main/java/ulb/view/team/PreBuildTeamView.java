package ulb.view.team;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import ulb.models.team.Team;
import javafx.scene.layout.VBox;
import java.util.UUID;

public class PreBuildTeamView extends BorderPane {
    private final ListView<Team> ListTeams = new ListView<>();
    private final Button renameButton = new Button("Rename");
    private final Button deleteButton = new Button("Delete");

    public PreBuildTeamView() {
        setPadding(new Insets(10));

        setCenter(ListTeams);

        HBox buttons = new HBox(10, renameButton, deleteButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox bottom = new VBox(buttons);
        bottom.setPadding(new Insets(10));
        setBottom(bottom);
        InitFakeTeam();
    }

    private void InitFakeTeam(){
        ListTeams.getItems().add(new Team("1 équipe", UUID.randomUUID()));
        ListTeams.getItems().add(new Team("2 équipe", UUID.randomUUID()));
    }
}
