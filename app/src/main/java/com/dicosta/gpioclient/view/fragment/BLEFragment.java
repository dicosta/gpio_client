package com.dicosta.gpioclient.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicosta.gpioclient.R;
import com.dicosta.gpioclient.contracts.BLEFlow;


public class BLEFragment extends BaseFragment implements BLEFlow{

    public BLEFragment() {
    }

    public static BLEFragment newInstance() {
        return new BLEFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ble, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigateToScan();
    }

    @Override
    public void navigateToScan() {
        replaceChildFragment(R.id.ble_container, BLEScanFragment.newInstance());
    }

    @Override
    public void navigateToLightsList(String macAddress) {
        replaceChildFragment(R.id.ble_container, BLEDeviceFragment.newInstance(macAddress));
    }
}
