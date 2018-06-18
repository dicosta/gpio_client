package com.dicosta.gpioclient.view;

import com.dicosta.gpioclient.viewmodel.ScanResultViewModel;

public interface BLEScanView extends MVPView {

    void requestEnableBluetooth();

    void requestLocationPermission();

    void showScanError(String message);

    void addScanResult(ScanResultViewModel scanResultViewModel);
}
