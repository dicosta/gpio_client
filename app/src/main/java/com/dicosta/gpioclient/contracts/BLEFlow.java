package com.dicosta.gpioclient.contracts;

/**
 * Created by diego on 24/01/18.
 */

public interface BLEFlow {
    void navigateToScan();
    void navigateToLightsList(String macAddress);
}
