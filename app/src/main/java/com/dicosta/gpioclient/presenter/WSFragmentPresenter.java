package com.dicosta.gpioclient.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Handler;

import com.dicosta.gpioclient.api.WSClient;
import com.dicosta.gpioclient.contracts.LightsView;
import com.dicosta.gpioclient.domain.Light;

import java.util.List;


/**
 * Created by diego on 22/01/18.
 */

public class WSFragmentPresenter implements LifecycleObserver {
    private LightsView mView;
    private WSClient mWSClient;

    public WSFragmentPresenter(LightsView view) {
        mView = view;
        ((LifecycleOwner) view).getLifecycle().addObserver(this);
        mWSClient = new WSClient();
    }


    public void turnLightOn(int pinNumber) {
        mWSClient.send(pinNumber, Light.STATE_ON);
    }

    public void turnLightOff(int pinNumber) {
        mWSClient.send(pinNumber, Light.STATE_OFF);
    }

    public void startLightBlink(int pinNumber) {
        mWSClient.send(pinNumber, Light.STATE_BLINK);
    }

    public void stopLightBlink(int pinNumber) {
        mWSClient.send(pinNumber, Light.STATE_OFF);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        //TODO: Preserve this connection between rotations in ViewModel object
        mWSClient.connect(new WSClient.WSLightClientListener() {
            @Override
            public void onLightsReceived(List<Light> lights) {
                mView.setLights(lights);
            }
        });
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onAttach() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onDetach() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        //TODO: Release resources;
        mWSClient.disconnect();
    }
}
