package services.dataprovider;

import exceptions.AttackNotFoundException;
import exceptions.BugemonNotFoundException;
import exceptions.JsonResourceNotFoundException;
import exceptions.LoadingAddedBugemonFailedException;
import models.bugemon.Bugemon;
import models.bugemon.dto.BugemonDTO;
import repositories.CustomBugemonRepository;
import repositories.BugemonRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<Bugemon> getAllBugemons() throws JsonResourceNotFoundException, LoadingAddedBugemonFailedException {
        List<Bugemon> allBugemons = new ArrayList<>(BugemonRepository.getInstance().getAllBugemons());
        List<Bugemon> addedBugemons = CustomBugemonRepository.getInstance().loadAddedBugemon();
        for (Bugemon bug : addedBugemons) {
            try { initBugemonFromSavedTeam(bug); } catch (AttackNotFoundException ignored) {}
        }
        allBugemons.addAll(addedBugemons);
        allBugemons.sort(Comparator.comparing(Bugemon::getType));
        return allBugemons;
    }

    public List<BugemonDTO> getAllBugemonsDTO() throws JsonResourceNotFoundException, LoadingAddedBugemonFailedException {
        List<BugemonDTO> allDefaultBugemon = new ArrayList<>(BugemonRepository.getInstance().getAllBugemons());
        List<Bugemon> addedBugemons = CustomBugemonRepository.getInstance().loadAddedBugemon();
        for (Bugemon bug : addedBugemons) {
            try { initBugemonFromSavedTeam(bug); } catch (AttackNotFoundException ignored) {}
        }
        List<BugemonDTO> newList = Stream.concat(allDefaultBugemon.stream(), addedBugemons.stream())
                .sorted(Comparator.comparing(BugemonDTO::getType))
                .collect(Collectors.toList());
        return newList;
    }

    public void initBugemonFromSavedTeam(Bugemon bugemon) throws AttackNotFoundException, JsonResourceNotFoundException {
        assignAttacks(bugemon);
        bugemon.initPv();
        bugemon.initColor();
        bugemon.initAttackColors();
    }
}