package models.bugemon.dto;

import models.bugemon.Attack;

import java.util.List;

/**
 * Read-only interface exposing the display data of a Bugémon.
 * Passed to views so they can read Bugémon data without modifying the model.
 */
public interface BugemonDTO {

    /**
     * @return the unique identifier of the Bugémon
     */
    String getId();

    /**
     * @return the name of the Bugémon
     */
    String getNom();

    /**
     * @return the filename of the Bugémon's sprite image
     */
    String getSprite();

    /**
     * @return the list of attacks of the Bugémon
     */
    List<Attack> getAttacks();

    /**
     * @return the max HP of the Bugémon
     */
    int getPv();

    /**
     * @return the current HP of the Bugémon
     */
    int getcurrenPv();

    /**
     * @return the attack stat of the Bugémon
     */
    int getAttaque();

    /**
     * @return the defense stat of the Bugémon
     */
    int getDefense();

    /**
     * @return the initiative stat of the Bugémon
     */
    int getInitiative();

    /**
     * @return the attacks as a String
     */
    String getAttackNamesAsString();

    /**
     * @return the attacks as a List of String
     */
    List<String> getAttackNamesAsList();

    /**
     * @return tell if the bugemon is from the player team
     */
    boolean getIsPlayer();
}