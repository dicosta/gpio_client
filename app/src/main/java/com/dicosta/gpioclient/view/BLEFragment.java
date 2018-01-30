package com.dicosta.gpioclient.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicosta.gpioclient.R;
import com.dicosta.gpioclient.contracts.BLEFlow;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BLEFragment extends Fragment implements BLEFlow {

    private Unbinder mUnbinder;

    public BLEFragment() {
    }

    public static BLEFragment newInstance() {
        BLEFragment fragment = new BLEFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ble, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navigateToScan();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void navigateToScan() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ble_container, BLEScanFragment.newInstance());
        fragmentTransaction.commit();
    }

    @Override
    public void navigateToLightsList(String macAddress) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.ble_container, BLEDeviceFragment.newInstance(macAddress));
        fragmentTransaction.commit();

    }
}
