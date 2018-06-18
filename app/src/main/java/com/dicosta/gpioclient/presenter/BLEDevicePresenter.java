package com.dicosta.gpioclient.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.dicosta.gpioclient.ble.GattClient;
import com.dicosta.gpioclient.domain.Light;
import com.dicosta.gpioclient.net.SerializerProvider;
import com.dicosta.gpioclient.view.BLEDeviceView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.RxBleDevice;
import com.polidea.rxandroidble2.helpers.ValueInterpreter;
import com.polidea.rxandroidble2.internal.RxBleDeviceProvider;

import org.reactivestreams.Subscription;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by diego on 24/01/18.
 */

public class BLEDevicePresenter extends BasePresenter<BLEDeviceView> {

    private PublishSubject<Void> disconnectTriggerSubject = PublishSubject.create();

    private RxBleConnection mConnection;
    private Subscription mIndicationSuscription;


    private final UUID readNotifyCharacteristic = UUID.fromString("13333333-3333-3333-3333-333333330002");
    private final UUID writeCharacteristic = UUID.fromString("13333333-3333-3333-3333-333333330003");

    private StringBuilder mIndicationMessageBuilder = new StringBuilder();

    private final RxBleClient mRxBleClient;
    private final Gson mGson;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    BLEDevicePresenter(BLEDeviceView view, GattClient gattClient, SerializerProvider serializerProvider) {
        super(view);

        mRxBleClient = gattClient.getRXBleClient();
        mGson = serializerProvider.getGson();
    }

    @Override
    public void onEnd() {
        super.onEnd();
        compositeDisposable.clear();
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

        Disposable writeDisposable = mConnection
                .writeCharacteristic(writeCharacteristic, payload.getBytes())
                //.takeUntil(disconnectTriggerSubject)
                //.first()
                .subscribe(this::onWriteSuccessful, this::onWriteFailed);

        compositeDisposable.add(writeDisposable);
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

        Disposable readDisposable = connection
                .readCharacteristic(readNotifyCharacteristic)
                //.takeUntil(disconnectTriggerSubject)
                //.first()
                .subscribe(this::onReadReceived, this::onReadFailed);

        compositeDisposable.add(readDisposable);

        Log.d("GPIOCLIENT", "ATTEMPTING TO SUSCRIBE TO INDICATE");

        Disposable setupIndicationDisposable = connection
                .setupIndication(readNotifyCharacteristic)
                .takeUntil(disconnectTriggerSubject)
                .flatMap(notificationObservable -> notificationObservable)
                .subscribe(this::onIndicationReceived, this::onIndicationSetupFailure);

        compositeDisposable.add(setupIndicationDisposable);
    }

    private void onReadReceived(byte[] bytes) {
        Type listType = new TypeToken<ArrayList<Light>>(){}.getType();
        List<Light> retList = mGson.fromJson(ValueInterpreter.getStringValue(bytes, 0), listType);
        view.setLights(retList);
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
            view.setLights(retList);

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
        RxBleDevice bleDevice = mRxBleClient.getBleDevice(macAddress);

        Disposable connectionDisposable = bleDevice.establishConnection(false)
                .takeUntil(disconnectTriggerSubject)
                .subscribe(this::onConnectionReceived, this::onConnectionFailure);

        compositeDisposable.add(connectionDisposable);
    }

    private void triggerDisconnect() {
        disconnectTriggerSubject.onNext(null);
    }
}
