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
import com.dicosta.gpioclient.presenter.WSFragmentPresenter;
import com.dicosta.gpioclient.view.WSView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class WSFragment extends BaseMVPFragment<WSFragmentPresenter> implements WSView {

    @BindView(R.id.lights_list)
    RecyclerView mLightsList;

    private LightItemAdapter mLightItemAdapter;

    public WSFragment() {
        // Required empty public constructor
    }

    public static WSFragment newInstance() {
        return new WSFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ws, container, false);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        mLightItemAdapter = new LightItemAdapter();

        mLightItemAdapter.setItems(new ArrayList<>());

        mLightsList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mLightsList.setAdapter(mLightItemAdapter);

        mLightItemAdapter.setLightItemAdapterListener(new LightItemAdapter.LightItemAdapterListener() {
            @Override
            public void onSwitchTurnOnClicked(Light light) {
                presenter.turnLightOn(light.getId());
            }

            @Override
            public void onSwitchTurnOffClicked(Light light) {
                presenter.turnLightOff(light.getId());
            }

            @Override
            public void onStartBlinkClicked(Light light) {
                presenter.startLightBlink(light.getId());
            }

            @Override
            public void onStopBlinkClicked(Light light) {
                presenter.stopLightBlink(light.getId());
            }
        });
    }

    @Override
    public void setLights(List<Light> lightsList) {
        mLightItemAdapter.setItems(lightsList);
    }
}
