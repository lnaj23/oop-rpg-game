package controllers.fight;

import controllers.Controller;
import controllers.MetaController;
import models.bugemon.dto.BugemonDTO;
import views.fight.SwitchBugemonSubView;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class SwitchBugemonController extends Controller<SwitchBugemonSubView> {

    private final Object currentOnFieldId;
    private final Consumer<Object> onBugemonSelected;
    private final Runnable onCancel;

    public SwitchBugemonController(MetaController metaController,
                                   List<BugemonDTO> team,
                                   boolean onPurpose,
                                   Object currentOnFieldId,
                                   Consumer<Object> onBugemonSelected,
                                   Runnable onCancel) throws IOException {
        super(metaController, new SwitchBugemonSubView());
        this.currentOnFieldId = currentOnFieldId;
        this.onBugemonSelected = onBugemonSelected;
        this.onCancel = onCancel;

        this.view.setController(this);
        this.view.displayTeam(team, onPurpose, currentOnFieldId);
    }

    public void onBugemonClicked(BugemonDTO bugemon) {
        if (bugemon.getcurrenPv() <= 0) {
            view.showMessageKO("Erreur", "Ce Bugemon est KO.");
            return;
        }

        if (bugemon.getId().equals(currentOnFieldId)) {
            view.showMessageKO("Erreur", "Ce Bugemon est deja sur le terrain !");
            return;
        }

        view.closeWindow();
        onBugemonSelected.accept(bugemon.getId());
    }

    public void onCancelClicked() {
        view.closeWindow();
        onCancel.run();
    }

    public SwitchBugemonSubView getView() {
        return view;
    }
}