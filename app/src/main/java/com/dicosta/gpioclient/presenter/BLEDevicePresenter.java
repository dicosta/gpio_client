package com.dicosta.gpioclient.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.dicosta.gpioclient.ble.GattClient;
import com.dicosta.gpioclient.contracts.DeviceView;
import com.dicosta.gpioclient.domain.Light;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.helpers.ValueInterpreter;

import org.reactivestreams.Subscription;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.subjects.PublishSubject;

/**
 * Created by diego on 24/01/18.
 */

public class BLEDevicePresenter implements LifecycleObserver {

    private PublishSubject<Void> disconnectTriggerSubject = PublishSubject.create();

    private DeviceView mView;
    private RxBleConnection mConnection;
    private Subscription mIndicationSuscription;
    private Gson mGson = new Gson();

    private final UUID readNotifyCharacteristic = UUID.fromString("13333333-3333-3333-3333-333333330002");
    private final UUID writeCharacteristic = UUID.fromString("13333333-3333-3333-3333-333333330003");

    private StringBuilder mIndicationMessageBuilder = new StringBuilder();

    public BLEDevicePresenter(DeviceView view) {
        mView = view;
        ((LifecycleOwner) view).getLifecycle().addObserver(this);
    }

    private void onConnectionFailure(Throwable throwable) {
        Log.d("GPIOCLIENT", "CONNECTION FAILURE: " + throwable.getMessage());
    }

    public void turnOn(Light light) {
        write(light.getId(), Light.STATE_ON);
    }

    public void turnOff(Light light) {
        write(light.getId(), Light.STATE_OFF);
    }

    public void startBlink(Light light) {
        write(light.getId(), Light.STATE_BLINK);
    }

    public void stopBlink(Light light) {
        write(light.getId(), Light.STATE_OFF);
    }

    private void write(int id, String state) {
        String payload = null;//= mGson.toJson(new LightWriteAction(id, state));

        mConnection
                .writeCharacteristic(writeCharacteristic, payload.getBytes())
                //.takeUntil(disconnectTriggerSubject)
                //.first()
                .subscribe(this::onWriteSuccessful, this::onWriteFailed);
    }

    private void onWriteSuccessful(byte[] bytesWritten) {
        Log.d("GPIOCLIENT", "WRITE OK");
    }

    private void onWriteFailed(Throwable throwable) {
        Log.d("GPIOCLIENT", "WRITE FAILED: " + throwable.getMessage());
    }

    private void onConnectionReceived(RxBleConnection connection) {
        Log.d("GPIOCLIENT", "CONNECTION RECEIVED OK");
        Log.d("GPIOCLIENT", "ATTEMPTING TO READ");

        mConnection = connection;

        connection
                .readCharacteristic(readNotifyCharacteristic)
                //.takeUntil(disconnectTriggerSubject)
                //.first()
                .subscribe(this::onReadReceived, this::onReadFailed);

        Log.d("GPIOCLIENT", "ATTEMPTING TO SUSCRIBE TO INDICATE");

        connection
                .setupIndication(readNotifyCharacteristic)
                .takeUntil(disconnectTriggerSubject)
                .flatMap(notificationObservable -> notificationObservable)
                .subscribe(this::onIndicationReceived, this::onIndicationSetupFailure);
    }

    private void onReadReceived(byte[] bytes) {
        Type listType = new TypeToken<ArrayList<Light>>(){}.getType();
        List<Light> retList = mGson.fromJson(ValueInterpreter.getStringValue(bytes, 0), listType);
        mView.setLights(retList);
    }

    private void onReadFailed(Throwable throwable) {
        Log.d("GPIOCLIENT", "READ FAILURE: " + throwable.getMessage());
    }

    private void onIndicationReceived(byte[] bytes) {
        Log.d("GPIOCLIENT", "Packet Received: " + ValueInterpreter.getStringValue(bytes, 0));

        //20b mtu hardcoded on gatt server.
        //TODO: negotiate new mtu.
        if (bytes.length < 20) {
            mIndicationMessageBuilder.append(ValueInterpreter.getStringValue(bytes, 0));

            Type listType = new TypeToken<ArrayList<Light>>(){}.getType();
            List<Light> retList = mGson.fromJson(mIndicationMessageBuilder.toString(), listType);
            mView.setLights(retList);

            mIndicationMessageBuilder = new StringBuilder();
        } else {
            mIndicationMessageBuilder.append(ValueInterpreter.getStringValue(bytes, 0));
        }
    }

    private void onIndicationSetupFailure(Throwable throwable) {
        Log.d("GPIOCLIENT", "INDICATION SETUP FAILURE: " + throwable.getMessage());
    }

    private void clearSubscription() {
        mConnection = null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        triggerDisconnect();
    }

    public void connectToDevice(String macAddress) {
        RxBleDevice bleDevice = GattClient.getRXClient().getBleDevice(macAddress);

        bleDevice.establishConnection(false)
            .takeUntil(disconnectTriggerSubject)
            //.doOnUnsubscribe(this::clearSubscription)
            .subscribe(this::onConnectionReceived, this::onConnectionFailure);
    }

    private void triggerDisconnect() {
        disconnectTriggerSubject.onNext(null);
    }
}
