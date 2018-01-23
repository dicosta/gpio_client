package com.dicosta.gpioclient.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.text.TextUtils;
import android.util.Log;

import com.dicosta.gpioclient.ble.GattClient;
import com.dicosta.gpioclient.contracts.ScanView;
import com.dicosta.gpioclient.viewmodel.ScanResultViewModel;
import com.polidea.rxandroidble.exceptions.BleScanException;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Subscription;

/**
 * Created by diego on 23/01/18.
 */

public class BLEScanPresenter implements LifecycleObserver {

    static final String TAG = BLEScanPresenter.class.getSimpleName();

    private ScanView mView;
    private Subscription mScanSubscription;

    public BLEScanPresenter(ScanView view) {
        mView = view;
        ((LifecycleOwner) view).getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (mScanSubscription != null) {
            mScanSubscription.unsubscribe();
        }
    }

    public void startScan() {
        //NOTE: RXBleClient still stuck in RX1.x
        mScanSubscription = GattClient.getRXClient().scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                        .build(),
                ScanFilter.empty())
        .doOnUnsubscribe(() -> Log.d(TAG, "Unsuscribed From Scanner"))
        .subscribe(this::handleScanResultItem, this::handleScanError
        );
    }

    private void handleScanResultItem(ScanResult scanResult) {
        mView.addScanResult(new ScanResultViewModel.Builder()
                .setMacAddress(scanResult.getBleDevice().getMacAddress())
                .setName(scanResult.getBleDevice().getName())
                .setRssi(scanResult.getRssi())
                .build());
    }

    private void handleScanError(Throwable throwable) {
        final String text;

        if (throwable instanceof BleScanException) {
            BleScanException bleScanException = (BleScanException) throwable;

            switch (bleScanException.getReason()) {
                case BleScanException.BLUETOOTH_NOT_AVAILABLE:
                    text = "Bluetooth is not available";
                    break;
                case BleScanException.BLUETOOTH_DISABLED:
                    text = "Enable bluetooth and try again";
                    mView.requestEnableBluetooth();
                    break;
                case BleScanException.LOCATION_PERMISSION_MISSING:
                    text = null;
                    mView.requestLocationPermission();
                    break;
                case BleScanException.LOCATION_SERVICES_DISABLED:
                    text = "Location services needs to be enabled on Android 6.0";
                    break;
                case BleScanException.SCAN_FAILED_ALREADY_STARTED:
                    text = "Scan with the same filters is already started";
                    break;
                case BleScanException.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
                    text = "Failed to register application for bluetooth scan";
                    break;
                case BleScanException.SCAN_FAILED_FEATURE_UNSUPPORTED:
                    text = "Scan with specified parameters is not supported";
                    break;
                case BleScanException.SCAN_FAILED_INTERNAL_ERROR:
                    text = "Scan failed due to internal error";
                    break;
                case BleScanException.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES:
                    text = "Scan cannot start due to limited hardware resources";
                    break;
                case BleScanException.UNDOCUMENTED_SCAN_THROTTLE:
                    text = String.format(
                            Locale.getDefault(),
                            "Android 7+ does not allow more scans. Try in %d seconds",
                            secondsTill(bleScanException.getRetryDateSuggestion())
                    );
                    break;
                case BleScanException.UNKNOWN_ERROR_CODE:
                case BleScanException.BLUETOOTH_CANNOT_START:
                default:
                    text = "Unable to start scanning";
                    break;
            }

            if (!TextUtils.isEmpty(text)) {
                Log.w("EXCEPTION", text, bleScanException);
                mView.showScanError(text);
            }
        }
    }

    private long secondsTill(Date retryDateSuggestion) {
        return TimeUnit.MILLISECONDS.toSeconds(retryDateSuggestion.getTime() - System.currentTimeMillis());
    }
}
