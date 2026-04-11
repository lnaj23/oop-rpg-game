package services.fight;

import java.util.List;

public class EffectResult {
    private final List<String> messages;
    public EffectResult(List<String> messages) { this.messages = messages; }
    public List<String> getMessages() { return messages; }
}
