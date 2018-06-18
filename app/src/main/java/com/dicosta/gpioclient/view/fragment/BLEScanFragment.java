package com.dicosta.gpioclient.view.fragment;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.dicosta.gpioclient.R;
import com.dicosta.gpioclient.adapter.ScanResultAdapter;
import com.dicosta.gpioclient.contracts.BLEFlow;
import com.dicosta.gpioclient.presenter.BLEScanPresenter;
import com.dicosta.gpioclient.view.BLEScanView;
import com.dicosta.gpioclient.viewmodel.ScanResultViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Created by diego on 22/01/18.
 */

public class BLEScanFragment extends BaseMVPFragment<BLEScanPresenter> implements BLEScanView {

    public static final int LOCATION_PERMISSION_REQUEST = 99;
    public static final int ENABLE_BT_REQUEST = 98;

    @BindView(R.id.scan_button)
    Button scanButton;
    @BindView(R.id.scan_progress)
    ContentLoadingProgressBar scanProgressBar;
    @BindView(R.id.scan_result_list)
    RecyclerView scanResultList;

    private Unbinder mUnbinder;
    private ScanResultAdapter mAdapter;
    private BLEFlow mBLEFlow;

    public BLEScanFragment() {
    }

    public static BLEScanFragment newInstance() {
        return new BLEScanFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getParentFragment() != null && getParentFragment() instanceof BLEFlow) {
            mBLEFlow = (BLEFlow)getParentFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ble_scan, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new ScanResultAdapter();
        scanResultList.setLayoutManager(new LinearLayoutManager(getContext()));
        scanResultList.setAdapter(mAdapter);

        mAdapter.setOnScanResultClickListener(macAddress -> mBLEFlow.navigateToLightsList(macAddress));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }

    @OnClick(R.id.scan_button)
    public void scanButtonClicked(View view) {
        presenter.startScan();

        scanButton.setVisibility(View.GONE);
        scanProgressBar.show();
    }

    @Override
    public void showScanError(String message) {
        scanButton.setVisibility(View.VISIBLE);
        scanProgressBar.hide();

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void addScanResult(ScanResultViewModel scanResultViewModel) {
        if (scanResultList.getVisibility() != View.VISIBLE) {
            scanResultList.setVisibility(View.VISIBLE);
            scanProgressBar.hide();
        }

        mAdapter.addScanResult(scanResultViewModel);
    }

    @Override
    public void requestEnableBluetooth() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, ENABLE_BT_REQUEST);
    }

    @Override
    public void requestLocationPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.title_location_permission)
                    .setMessage(R.string.text_location_permission)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                        //Prompt the user once explanation has been shown
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST);
                    })
                    .create()
                    .show();
        } else {
            // No explanation needed, we can request the permission.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        presenter.startScan();
                    }
                } else {
                    scanButton.setVisibility(View.VISIBLE);
                    scanProgressBar.hide();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_BT_REQUEST && resultCode == RESULT_OK) {
            presenter.startScan();
        }
    }
}
