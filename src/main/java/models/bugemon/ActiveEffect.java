package models.bugemon;

public class ActiveEffect {
    private Effect effect;
    private Integer turnsLeft;

    public ActiveEffect(Effect effect) {
        this.effect = effect;
        if ("1_tour".equals(effect.getDuration())) {
            this.turnsLeft = 1;
        } else {
            this.turnsLeft = null;
        }
    }

    public Effect getEffect() { return effect; }

    public Integer getTurnsLeft() { return turnsLeft; }

    public void decrementTurns() { if (turnsLeft != null) turnsLeft--; }

    public boolean isExpired() {
        return turnsLeft != null && turnsLeft <= 0;
    }
}

