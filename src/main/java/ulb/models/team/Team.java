package ulb.models.team;

import java.util.UUID;

public class Team {
    public String name = "";
    public UUID id;

    public Team(String name ,UUID id) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name; // ou this.name
    }

    public String getName() {
        return this.name;
    }
}
