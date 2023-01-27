package cz.vse.nulltracker.nulltracker.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Exercise {
    private String name;
    private String description;
    private Map parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public Exercise(String name, String description, Map parameters) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return name;
    }
}