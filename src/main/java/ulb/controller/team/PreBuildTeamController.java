package ulb.controller.team;

import javafx.scene.control.Alert;
import ulb.models.team.Team;
import ulb.service.team.PreBuildTeamService;
import ulb.view.team.PreBuildTeamView;

public class PreBuildTeamController {
    private final PreBuildTeamView view;
    private final PreBuildTeamService service;

    public PreBuildTeamController(PreBuildTeamView preView, PreBuildTeamService preService){
        this.view = preView;
        this.service = preService;
    }

    public void init(){
        // Faudra ajouter un refresh des teams, juste une fonction qui demande à view de le faire

        view.getStartButton().setOnAction(e -> showError("Not Available", "Temporarily unavailable"));
        view.getModifyButton().cancelButtonProperty();
        view.getDeleteButton().setOnAction(e -> DeleteTeam());
        view.getRenameButton().setOnAction(e -> RenameTeam());
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void DeleteTeam(){
        Team selectedTeam = view.getSelectedTeam();
        if (selectedTeam == null) {
            showError("Problem", "You have to select a team before clicking any button");
        }
    }

    private void RenameTeam(){

    }



}
