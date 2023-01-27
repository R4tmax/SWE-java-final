package cz.vse.nulltracker.nulltracker.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Exercise {

    @SerializedName("type")
    @Expose
    private Object type;

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

}