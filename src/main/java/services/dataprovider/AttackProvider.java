package services.dataprovider;

import exceptions.AttackNotFoundException;
import exceptions.JsonResourceNotFoundException;
import models.bugemon.Attack;
import repositories.AttackRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AttackProvider {

    public Attack getAttackById(String id) throws AttackNotFoundException, JsonResourceNotFoundException {
        for (Attack attack : AttackRepository.getInstance().getAllAttacks()) {
            if (attack.getId().equals(id) || attack.getName().equals(id)) return new Attack(attack);
        }
        throw new AttackNotFoundException(id);
    }

    public List<Attack> getAllAttacks() throws JsonResourceNotFoundException {
        return new ArrayList<>(AttackRepository.getInstance().getAllAttacks());
    }

    public Attack getRandomAttack() throws JsonResourceNotFoundException {
        List<Attack> attacks = getAllAttacks();
        return attacks.get(new Random().nextInt(attacks.size()));
    }

}