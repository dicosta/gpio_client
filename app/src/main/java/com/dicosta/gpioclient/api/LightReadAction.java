package com.dicosta.gpioclient.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by diego on 22/01/18.
 */

public class LightReadAction {

    @SerializedName("action")
    private String action = "READ";


    @SerializedName("id")
    private Integer id;

    public LightReadAction() {
    }

    public LightReadAction(int id) {
        this.id = id;
    }
}
