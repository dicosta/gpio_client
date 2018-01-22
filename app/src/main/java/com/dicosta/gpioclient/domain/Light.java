package com.dicosta.gpioclient.domain;

import android.support.annotation.StringDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by diego on 17/01/18.
 */

public class Light {
    @Retention(SOURCE)
    @StringDef({
            STATE_ON,
            STATE_OFF,
            STATE_DIM,
            STATE_BLINK
    })
    public @interface LightState {}

    @Retention(SOURCE)
    @StringDef({
            COLOR_RED,
            COLOR_YELLOW,
            COLOR_GREEN
    })
    public @interface LightColor {}

    public static final String COLOR_RED = "RED";
    public static final String COLOR_YELLOW = "YELLOW";
    public static final String COLOR_GREEN = "GREEN";

    public static final String STATE_ON = "ON";
    public static final String STATE_OFF = "OFF";
    public static final String STATE_DIM = "DIM";
    public static final String STATE_BLINK = "BLINK";

    @SerializedName("id")
    private int id;
    @SerializedName("dimmable")
    private boolean isDimmable;
    @SerializedName("name")
    private @LightState String name;
    @SerializedName("color")
    private @LightColor String color;
    @SerializedName("state")
    private String state;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDimmable() {
        return isDimmable;
    }

    public void setDimmable(boolean dimmable) {
        isDimmable = dimmable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @LightColor String getColor() {
        return color;
    }

    public void setColor(@LightColor String color) {
        this.color = color;
    }

    public @LightState String getState() {
        return state;
    }

    public void setState(@LightState String state) {
        this.state = state;
    }

    public boolean isLightOn() {
        return STATE_ON.equals(state);
    }

    public boolean isLightOff() {
        return STATE_OFF.equals(state);
    }

    public boolean isLightDim() {
        return STATE_DIM.equals(state);
    }

    public boolean isLighBlink() {
        return STATE_BLINK.equals(state);
    }
}
