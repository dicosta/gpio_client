package com.dicosta.gpioclient.presenter;

import android.os.Handler;
import com.dicosta.gpioclient.api.LightAPI;
import com.dicosta.gpioclient.domain.Light;
import com.dicosta.gpioclient.domain.LightState;
import com.dicosta.gpioclient.net.HttpServiceFactory;
import com.dicosta.gpioclient.view.HTTPView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
/**
 * Created by diego on 19/01/18.
 */

public class HTTPFragmentPresenter extends BasePresenter<HTTPView> {

    private final int POLLING_FREQUENCY = 2000;
    private Handler mHandler = new Handler();
    private LightAPI mLightAPI;
    private CompositeDisposable disposeBag = new CompositeDisposable();

    @Inject
    HTTPFragmentPresenter(HTTPView view/*, HttpServiceFactory httpServiceFactory*/,LightAPI lightAPI) {
        super(view);

        mLightAPI = lightAPI;
        //mLightAPI = httpServiceFactory.create(LightAPI.class);
    }

    @Override
    public void onEnd() {
        super.onEnd();
        stopPolling();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPolling();

        disposeBag.clear();
    }

    @Override
    public void onResume() {
        super.onResume();

        startPolling();
    }

    public void turnLightOn(int pinNumber) {
        LightState newState = new LightState();
        newState.setState(Light.STATE_ON);
        sendLightState(pinNumber, newState);
    }

    public void turnLightOff(int pinNumber) {
        LightState newState = new LightState();
        newState.setState(Light.STATE_OFF);
        sendLightState(pinNumber, newState);
    }

    public void startLightBlink(int pinNumber) {
        LightState newState = new LightState();
        newState.setState(Light.STATE_BLINK);
        sendLightState(pinNumber, newState);
    }

    public void stopLightBlink(int pinNumber) {
        LightState newState = new LightState();
        newState.setState(Light.STATE_OFF);
        sendLightState(pinNumber, newState);
    }

    private void sendLightState(int pinNumber, LightState state) {
        stopPolling();

        Disposable disposable = mLightAPI.writeLight(pinNumber, state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::startPolling,
                        error -> { startPolling(); handleError(error);});

        disposeBag.add(disposable);
    }

    private Runnable getAllLightsFetchRunnable() {
        return () -> {
            Disposable disposable = mLightAPI.getAllLights()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleReturnedData, this::handleError);

            disposeBag.add(disposable);
        };
    }

    private void handleReturnedData(List<Light> lightList) {
        view.setLights(lightList);
        mHandler.postDelayed(getAllLightsFetchRunnable(), POLLING_FREQUENCY);
    }

    private void handleError(Throwable error) {
    }

    private void startPolling() {
        mHandler.post(getAllLightsFetchRunnable());
    }

    private void stopPolling() {
        mHandler.removeCallbacksAndMessages(null);
    }
}
