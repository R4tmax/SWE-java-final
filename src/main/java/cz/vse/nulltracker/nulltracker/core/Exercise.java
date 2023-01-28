package cz.vse.nulltracker.nulltracker.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class Exercise {
    private String name;
    private String description;
    private ArrayList<String> parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }


    public ArrayList<String> getParameters() {
        return parameters;
    }

    public Exercise(String name, String description, ArrayList<String> parameters) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return name;
    }
}