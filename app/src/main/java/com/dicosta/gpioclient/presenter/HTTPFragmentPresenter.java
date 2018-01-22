package com.dicosta.gpioclient.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.util.Log;

import com.dicosta.gpioclient.BuildConfig;
import com.dicosta.gpioclient.api.LightAPI;
import com.dicosta.gpioclient.contracts.LightsView;
import com.dicosta.gpioclient.domain.Light;
import com.dicosta.gpioclient.domain.LightState;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by diego on 19/01/18.
 */

public class HTTPFragmentPresenter implements LifecycleObserver {

    private int POLLING_FREQUENCY = 2000;
    private Handler mHandler = new Handler();
    private LightsView mView;
    private LightAPI mLightAPI;
    private CompositeDisposable disposeBag;

    public HTTPFragmentPresenter(LightsView view) {
        mView = view;
        disposeBag = new CompositeDisposable();
        ((LifecycleOwner) view).getLifecycle().addObserver(this);
    }

    private void sendLightState(int pinNumber, LightState state) {
        Disposable disposable = mLightAPI.writeLight(pinNumber, state)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        disposeBag.add(disposable);
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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mLightAPI = retrofit.create(LightAPI.class);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onAttach() {
        startPolling();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onDetach() {
        stopPolling();
        disposeBag.clear();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        Log.d("LC", "DESTROY");
        //TODO: Release resources;
    }

    private void handleReturnedData(List<Light> lightList) {
        mView.setLights(lightList);
        mHandler.postDelayed(getAllLightsFetchRunnable(), POLLING_FREQUENCY);
    }

    private void handleDataSent() {
    }

    private void handleError(Throwable error) {
    }

    private Runnable getAllLightsFetchRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                Disposable disposable = mLightAPI.getAllLights()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> handleReturnedData(result),
                                error -> handleError(error));

                disposeBag.add(disposable);
            }
        };
    }

    private void startPolling() {
        mHandler.post(getAllLightsFetchRunnable());
    }

    private void stopPolling() {
        mHandler.removeCallbacksAndMessages(null);
    }
}
