package cz.vse.nulltracker.nulltracker.database;

import java.util.Map;

public class LoggedActivity {
    private String name;
    private final Map<String,Double> parameters;

    public LoggedActivity(String name, Map<String, Double> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public Map<String, Double> getParameters() {
        return parameters;
    }
}
