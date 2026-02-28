package ulb.controller.team;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import ulb.models.team.Team;
import ulb.models.team.PreBuildTeamService;
import ulb.view.team.PreBuildTeamView;

import java.util.Optional;

public class PreBuildTeamController {
    private final PreBuildTeamView view;
    private final PreBuildTeamService service;

    public PreBuildTeamController(PreBuildTeamView preView, PreBuildTeamService preService) {
        this.view = preView;
        this.service = preService;
    }

    public void init() {
        service.InitFakeTeam();

        // On lie la ListView à une liste qui envoie des signaux à celles-ci dès qu'il y a des changements dedans
        view.getListView().setItems(service.getTeams());

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

    private void DeleteTeam() {
        Team selectedTeam = view.getSelectedTeam();

        if (selectedTeam == null) {
            showError("Problem", "You need to select a team you want to delete");
        }

        else {
            // Cette partie vient de la documentation directement
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this team?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // On modifie simplement la liste et au
                service.deleteTeam(selectedTeam);
            }
        }
    }

    private void RenameTeam() {

    }



}
