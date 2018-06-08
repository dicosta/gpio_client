package com.dicosta.gpioclient.ble;

import com.dicosta.gpioclient.GPIOApplication;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.internal.RxBleLog;

/**
 * Created by diego on 22/01/18.
 */

public class GattClient {

    private static volatile GattClient INSTANCE;
    private RxBleClient mRXBleClient;

    private GattClient(){

        if (INSTANCE != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }

        //mRXBleClient = RxBleClient.create(GPIOApplication.getInstance());

        RxBleClient.setLogLevel(RxBleLog.VERBOSE);
    }

    public static GattClient getInstance() {
        if (INSTANCE == null) {
            synchronized (GattClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new GattClient();
                }
            }
        }

        return INSTANCE;
    }

    public static RxBleClient getRXClient() {
        return getInstance().mRXBleClient;
    }
}
