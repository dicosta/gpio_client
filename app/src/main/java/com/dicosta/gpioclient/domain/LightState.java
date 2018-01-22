package com.dicosta.gpioclient.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by diego on 18/01/18.
 */

public class LightState {

    @SerializedName("state")
    private String state;

    @SerializedName("brightness")
    private Float brightness;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Float getBrightness() {
        return brightness;
    }

    public void setBrightness(Float brightness) {
        this.brightness = brightness;
    }
}
