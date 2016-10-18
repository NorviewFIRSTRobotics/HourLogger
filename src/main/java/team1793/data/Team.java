package team1793.data;

import javax.swing.JComboBox;
import java.util.Arrays;
import java.util.Optional;
import java.util.Vector;

/**
 * Purpose:
 *
 * @author Tyler Marshall
 * @version 10/14/16
 */
public enum Team {
    PROGRAMMING("Programming"),
    MECHANICAL("Mechanical"),
    ELECTRICAL("Electrical"),
    SHOP("Shop"),
    MISSION("Mission"),
    DESIGN("Design"),
    UNKNOWN("Unknown");
    private String name;

    Team(String name) {
        this.name = name;
    }

    public static Team[] VALUES = values();

    public static Team getValue(final String name) {
        Optional<Team> team = Arrays.stream(VALUES).filter(n -> n.getName().equalsIgnoreCase(name)).findFirst();
        if(team.isPresent())
            return team.get();
        else
            return UNKNOWN;
    }
    public static JComboBox<String> getComboBox() {
        Vector<String> vector = Arrays.stream(Team.VALUES).map(Team::getName).collect(Vector::new, Vector::add,Vector::addAll);
        return new JComboBox(vector);
    }
    public String getName() {
        return name;
    }

}
