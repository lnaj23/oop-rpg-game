package models.bugemon;

public class ActiveEffect {
    private Effect effect;
    private Integer turnsLeft;

    public ActiveEffect(Effect effect) {
        this.effect = effect;
        int d = effect.getDurationInTurns();
        this.turnsLeft = d == -1 ? null : d + 1;
    }

    public Effect getEffect() { return effect; }

    public Integer getTurnsLeft() { return turnsLeft; }

    public void decrementTurns() { if (turnsLeft != null) turnsLeft--; }

    public boolean isExpired() {
        return turnsLeft != null && turnsLeft <= 0;
    }
}

