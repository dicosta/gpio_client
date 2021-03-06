package com.dicosta.gpioclient.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.dicosta.gpioclient.ble.GattClient;
import com.dicosta.gpioclient.view.BLEScanView;
import com.dicosta.gpioclient.viewmodel.ScanResultViewModel;
import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.exceptions.BleScanException;
import com.polidea.rxandroidble2.scan.ScanFilter;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by diego on 23/01/18.
 */
public class BLEScanPresenter extends BasePresenter<BLEScanView> {

    static final String TAG = BLEScanPresenter.class.getSimpleName();

    private RxBleClient mRxBleClient;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    BLEScanPresenter(BLEScanView view, GattClient gattClient) {
        super(view);
        mRxBleClient = gattClient.getRXBleClient();
    }

    @Override
    public void onEnd() {
        super.onEnd();
        compositeDisposable.clear();
    }

    public void startScan() {
        Disposable scanSubscription = mRxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                        .build(),
                    ScanFilter.empty())
                .subscribe(this::handleScanResultItem, this::handleScanError);
        compositeDisposable.add(scanSubscription);
    }

    private void handleScanResultItem(ScanResult scanResult) {
        view.addScanResult(new ScanResultViewModel.Builder()
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
                    view.requestEnableBluetooth();
                    break;
                case BleScanException.LOCATION_PERMISSION_MISSING:
                    text = null;
                    view.requestLocationPermission();
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
                view.showScanError(text);
            }
        }
    }

    private long secondsTill(Date retryDateSuggestion) {
        return TimeUnit.MILLISECONDS.toSeconds(retryDateSuggestion.getTime() - System.currentTimeMillis());
    }
}
