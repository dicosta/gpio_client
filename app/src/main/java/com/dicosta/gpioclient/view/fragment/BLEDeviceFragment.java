package com.dicosta.gpioclient.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicosta.gpioclient.R;
import com.dicosta.gpioclient.adapter.LightItemAdapter;
import com.dicosta.gpioclient.domain.Light;
import com.dicosta.gpioclient.presenter.BLEDevicePresenter;
import com.dicosta.gpioclient.view.BLEDeviceView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by diego on 24/01/18.
 */
public class BLEDeviceFragment extends BaseMVPFragment<BLEDevicePresenter> implements BLEDeviceView {

    public static final String EXTRA_DEVICE_MAC_ADDRESS = "com.dicosta.gpioclient.view.device_mac_address";

    private Unbinder mUnbinder;

    @BindView(R.id.lights_list)
    RecyclerView mLightsList;

    private LightItemAdapter mLightItemAdapter;

    public BLEDeviceFragment() {
    }

    public static BLEDeviceFragment newInstance(String macAddress) {
        BLEDeviceFragment fragment = new BLEDeviceFragment();
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_DEVICE_MAC_ADDRESS, macAddress);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ble_device, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(EXTRA_DEVICE_MAC_ADDRESS)) {
            presenter.connectToDevice(getArguments().getString(EXTRA_DEVICE_MAC_ADDRESS));
        }

        mLightItemAdapter = new LightItemAdapter();

        mLightItemAdapter.setItems(new ArrayList<>());

        mLightsList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mLightsList.setAdapter(mLightItemAdapter);

        mLightItemAdapter.setLightItemAdapterListener(new LightItemAdapter.LightItemAdapterListener() {
            @Override
            public void onSwitchTurnOnClicked(Light light) {
                presenter.turnOn(light);
            }

            @Override
            public void onSwitchTurnOffClicked(Light light) {
                presenter.turnOff(light);
            }

            @Override
            public void onStartBlinkClicked(Light light) {
                presenter.startBlink(light);
            }

            @Override
            public void onStopBlinkClicked(Light light) {
                presenter.stopBlink(light);
            }
        });
    }

    @Override
    public void setLights(List<Light> lightsList) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLightItemAdapter.setItems(lightsList);
            }
        });
    }
}
