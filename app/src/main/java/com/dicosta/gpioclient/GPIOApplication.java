package com.dicosta.gpioclient;

import android.app.Application;

/**
 * Created by diego on 23/01/18.
 */

public class GPIOApplication extends Application {

    private static GPIOApplication INSTANCE;

    public static GPIOApplication getInstance(){
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
