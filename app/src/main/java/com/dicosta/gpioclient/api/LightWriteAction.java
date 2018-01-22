package com.dicosta.gpioclient.api;

import com.dicosta.gpioclient.domain.Light;
import com.google.gson.annotations.SerializedName;

/**
 * Created by diego on 22/01/18.
 */

public class LightWriteAction {
    @SerializedName("action")
    private String action = "WRITE";


    @SerializedName("id")
    private int id;

    @SerializedName("state")
    private @Light.LightState String state;

    public LightWriteAction(int id, @Light.LightState String state) {
        this.id = id;
        this.state = state;
    }
}
