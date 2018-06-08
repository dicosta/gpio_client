package com.dicosta.gpioclient.presenter;

import com.dicosta.gpioclient.domain.Light;
import com.dicosta.gpioclient.net.WSClient;
import com.dicosta.gpioclient.view.WSView;

import java.util.List;

import javax.inject.Inject;


/**
 * Created by diego on 22/01/18.
 */

public class WSFragmentPresenter extends BasePresenter<WSView> {

    private WSClient mWSClient;

    @Inject
    WSFragmentPresenter(WSView view, WSClient wsClient) {
        super(view);

        mWSClient = wsClient;
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

    @Override
    public void onResume() {
        super.onResume();

        mWSClient.connect(view::setLights);
    }

    @Override
    public void onPause() {
        super.onPause();

        mWSClient.disconnect();
    }
}
