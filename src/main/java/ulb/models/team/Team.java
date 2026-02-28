package ulb.models.team;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.UUID;

public class Team {
    private final StringProperty name = new SimpleStringProperty();
    private final UUID id;

    public Team(String name, UUID id) {
        this.id = id;
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public Observable[] getObservables() {
        return new Observable[] { name };
    }

    @Override
    public String toString() {
        return name.get();
    }

    public String getName() {
        return name.get();
    }

    public void setName(String value) {
        name.set(value);
    }
}
