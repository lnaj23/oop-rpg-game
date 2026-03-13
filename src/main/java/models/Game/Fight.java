package models.Game;

import models.Bugemon.Attack;
import models.Bugemon.Bugemon;
import models.Bugemon.TeamBugemon;


/**
 * Represents a fight between two Bugémon teams.
 * The fight manages turn order, attacks and determines
 * when the battle is finished and which team has won.
 */
public class Fight {
    final private TeamBugemon teamPlayer;
    final private TeamBugemon teamCPU;
    private Bugemon playingBugemon;
    private Bugemon waitingBugemon;

    public Fight(TeamBugemon teamPlayer, TeamBugemon teamCPU) {
        this.teamPlayer = teamPlayer;
        this.teamCPU = teamCPU;

        for (Bugemon bug : this.teamPlayer.getTeam()) {
            bug.setIsPlayer(true);
            bug.initPv();
        }

        for (Bugemon bug : this.teamCPU.getTeam()) {
            bug.setIsPlayer(false);
            bug.initPv();
        }
    }

    public boolean playerCanStart() {
        int initiativeTeamPlayer = 0;
        int initiativeTeamCPU = 0;

        for (Bugemon bug: this.teamPlayer.getTeam()) {
            initiativeTeamPlayer += bug.getInitiative();
        }

        for (Bugemon bug: this.teamCPU.getTeam()) {
            initiativeTeamCPU += bug.getInitiative();
        }

        return initiativeTeamPlayer > initiativeTeamCPU; // '>' ou bien '>=' ?
    }

    private void switchRole() {
        Bugemon tmp = playingBugemon;
        playingBugemon = waitingBugemon;
        waitingBugemon = tmp;
    }

    private boolean isFinished() {
        TeamBugemon waitingTeam;
        if (teamPlayer.getOnFieldBugemon() == playingBugemon) {
            waitingTeam = teamCPU;
        }
        else {
            waitingTeam = teamPlayer;
        }
        return waitingTeam.getTeam().stream()
                .noneMatch(Bugemon::isStillStanding);

    }

    private boolean playerWon() {
        return teamCPU.getTeam().stream()
                .noneMatch(Bugemon::isStillStanding);
    }

    private void playingAttackWaiting(Attack attack) {
        playingBugemon.attack(attack, waitingBugemon);
        if (isFinished()) {
            if (playerWon()) {
                System.out.println("Vous avez battu le joueur d'en face.");
            } else {
                System.out.println("Vous avez perdu.");
            }
        }
    }

    public TeamBugemon getTeamPlayer() {
        return teamPlayer;
    }
    public TeamBugemon getTeamCPU() {
        return teamCPU;
    }
}
