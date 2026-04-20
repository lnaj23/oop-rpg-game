package services.dataprovider;

import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;
import exceptions.JsonResourceNotFoundException;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import repositories.BugemonRepository;

import java.util.ArrayList;
import java.util.List;

public class BugemonProvider {

    private static final AttackProvider attackProvider = new AttackProvider();
    private static boolean initialized = false;

    public BugemonProvider() throws AttackNotFoundException, JsonResourceNotFoundException {
        if (!initialized) {
            for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
                assignAttacks(bug);
            }
            BugemonRepository.getInstance().getAllBugemons().forEach(Bugemon::initColor);
            BugemonRepository.getInstance().getAllBugemons().forEach(Bugemon::initPv);
            initialized = true;
        }
    }

    private void assignAttacks(Bugemon bug) throws AttackNotFoundException, JsonResourceNotFoundException {
        if (!bug.getAttacks().isEmpty()) return;
        for (String id : bug.getAttackNamesAsList()) {
            bug.addAttacks(attackProvider.getAttackById(id));
        }
    }

    public List<Bugemon> getListBugemon(List<String> teamIds) throws BugemonNotFoundException, JsonResourceNotFoundException {
        List<Bugemon> list = new ArrayList<>();
        for (String id : teamIds) {
            list.add(getBugemon(id));
        }
        return list;
    }

    private Bugemon getBugemon(String id) throws BugemonNotFoundException, JsonResourceNotFoundException {
        for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
            if (bug.getId().equals(id)) return new Bugemon(bug);
        }
        throw new BugemonNotFoundException(id);
    }

    public List<Bugemon> getAllBugemons() throws JsonResourceNotFoundException {
        // faut une liste qui contient BugemonRepository.getInstance().getAllBugemons()
        // et AddedBugemonRepository.getInstance().loadAddedBugemon() en 1 seul liste
        // et tout les objet bugemon doivent être trié selon leut attribut Type (qui est un string)
        return new ArrayList<>(BugemonRepository.getInstance().getAllBugemons());
    }

    public List<BugemonDTO> getAllBugemonsDTO() throws JsonResourceNotFoundException {
        return new ArrayList<>(BugemonRepository.getInstance().getAllBugemons()); // + les bugemons de CustomBugemonRepositories;
    }

    public void initBugemonFromSavedTeam(Bugemon bugemon) throws AttackNotFoundException, JsonResourceNotFoundException {
        assignAttacks(bugemon);
        bugemon.initPv();
        bugemon.initColor();
        bugemon.initAttackColors();
    }
}