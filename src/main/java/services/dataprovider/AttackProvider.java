package services.dataprovider;

import exceptions.AttackNotFoundException;
import models.bugemon.Attack;
import repositories.AttackRepository;

import java.util.ArrayList;
import java.util.List;

public class AttackProvider {

    private static AttackProvider instance;

    private AttackProvider() {}

    public static AttackProvider getInstance() {
        if (instance == null) {
            instance = new AttackProvider();
        }
        return instance;
    }

    public Attack getAttackById(String id) throws AttackNotFoundException {
        for (Attack attack : AttackRepository.getAllAttacks()) {
            if (attack.getId().equals(id)) return new Attack(attack);
        }
        throw new AttackNotFoundException(id);
    }

    public List<Attack> getAllAttacks() {
        return new ArrayList<>(AttackRepository.getAllAttacks());
    }
}