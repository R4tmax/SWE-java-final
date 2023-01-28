package cz.vse.nulltracker.nulltracker.database;

import java.util.Map;

public class LoggedActivity {
    private String name;
    private Map<String,String> parameters;

    public LoggedActivity(String name, Map<String, String> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
