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
    private final Button startButton = new Button("Start a game");
    private final Button modifyButton = new Button("Modify the team");
    private final Button renameButton = new Button("Rename");
    private final Button deleteButton = new Button("Delete");

    public PreBuildTeamView() {
        setPadding(new Insets(10));

        setCenter(ListTeams);

        HBox buttons = new HBox(10, startButton, modifyButton, renameButton, deleteButton);
        buttons.setAlignment(Pos.CENTER_LEFT);

        VBox bottom = new VBox(buttons);
        bottom.setPadding(new Insets(10));
        setBottom(bottom);
        InitFakeTeam();
    }

    public Team getSelectedTeam() {
        return ListTeams.getSelectionModel().getSelectedItem();
    }

    public void InitFakeTeam(){
        ListTeams.getItems().add(new Team("1 équipe", UUID.randomUUID()));
        ListTeams.getItems().add(new Team("2 équipe", UUID.randomUUID()));
    }



    // Getters des variables privées utilisées dans controller.

    public ListView<Team> getListTeams() {
        return ListTeams;
    }
    public Button getStartButton() {
        return startButton;
    }
    public Button getModifyButton() {
        return modifyButton;
    }
    public Button getRenameButton() {
        return renameButton;
    }
    public Button getDeleteButton() {
        return deleteButton;
    }
}
