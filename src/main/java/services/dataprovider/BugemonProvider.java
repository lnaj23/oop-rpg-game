package services.dataprovider;

import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import repositories.BugemonRepository;

import java.util.ArrayList;
import java.util.List;

public class BugemonProvider {

    private static final AttackProvider attackProvider = new AttackProvider();
    private static boolean initialized = false;

    public BugemonProvider() {
        if (!initialized) {
            try {
                for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
                    assignAttacks(bug);
                }
                BugemonRepository.getInstance().getAllBugemons().forEach(Bugemon::initColor);
                BugemonRepository.getInstance().getAllBugemons().forEach(Bugemon::initPv);
                initialized = true;
            } catch (AttackNotFoundException e) {
                throw new IllegalStateException("Failed to assign attacks during initialization: " + e.getMessage(), e);
            }
        }
    }

    private void assignAttacks(Bugemon bug) throws AttackNotFoundException {
        if (!bug.getAttacks().isEmpty()) return;
        for (String id : bug.getAttackNamesAsList()) {
            bug.addAttacks(attackProvider.getAttackById(id));
        }
    }

    public List<Bugemon> getListBugemon(List<String> teamIds) throws BugemonNotFoundException {
        List<Bugemon> list = new ArrayList<>();
        for (String id : teamIds) {
            list.add(getBugemon(id));
        }
        return list;
    }

    private Bugemon getBugemon(String id) throws BugemonNotFoundException {
        for (Bugemon bug : BugemonRepository.getInstance().getAllBugemons()) {
            if (bug.getId().equals(id)) return new Bugemon(bug);
        }
        throw new BugemonNotFoundException(id);
    }

    public List<Bugemon> getAllBugemons() {
        return new ArrayList<>(BugemonRepository.getInstance().getAllBugemons());
    }

    public List<BugemonDTO> getAllBugemonsDTO() {
        return new ArrayList<>(BugemonRepository.getInstance().getAllBugemons());
    }

    public void initBugemonFromSavedTeam(Bugemon bugemon) {
        try {
            assignAttacks(bugemon);
        } catch (AttackNotFoundException e) {
            throw new IllegalStateException("Attack not found: " + e.getMessage(), e);
        }
        bugemon.initPv();
        bugemon.initColor();
        bugemon.initAttackColors();
    }
}