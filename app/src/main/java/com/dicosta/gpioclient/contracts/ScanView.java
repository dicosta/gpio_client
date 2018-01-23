package com.dicosta.gpioclient.contracts;

import com.dicosta.gpioclient.viewmodel.ScanResultViewModel;

/**
 * Created by diego on 23/01/18.
 */

public interface ScanView {

    void showScanError(String message);

    void addScanResult(ScanResultViewModel scanResultViewModel);

    void requestLocationPermission();

    void requestEnableBluetooth();
}
