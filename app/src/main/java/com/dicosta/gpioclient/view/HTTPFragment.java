package com.dicosta.gpioclient.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicosta.gpioclient.R;
import com.dicosta.gpioclient.adapter.LightItemAdapter;
import com.dicosta.gpioclient.contracts.LightsView;
import com.dicosta.gpioclient.domain.Light;
import com.dicosta.gpioclient.presenter.HTTPFragmentPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HTTPFragment extends Fragment implements LightsView {

    @BindView(R.id.lights_list)
    RecyclerView mLightsList;

    private Unbinder mUnbinder;
    private LightItemAdapter mLightItemAdapter;
    private HTTPFragmentPresenter mPresenter;

    public HTTPFragment() {
        // Required empty public constructor
    }

    public static HTTPFragment newInstance() {
        HTTPFragment fragment = new HTTPFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new HTTPFragmentPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_http, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLightItemAdapter = new LightItemAdapter();

        mLightItemAdapter.setItems(new ArrayList<>());

        mLightsList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mLightsList.setAdapter(mLightItemAdapter);

        mLightItemAdapter.setLightItemAdapterListener(new LightItemAdapter.LightItemAdapterListener() {
            @Override
            public void onSwitchTurnOnClicked(Light light) {
                mPresenter.turnLightOn(light.getId());
            }

            @Override
            public void onSwitchTurnOffClicked(Light light) {
                mPresenter.turnLightOff(light.getId());
            }

            @Override
            public void onStartBlinkClicked(Light light) {
                mPresenter.startLightBlink(light.getId());
            }

            @Override
            public void onStopBlinkClicked(Light light) {
                mPresenter.stopLightBlink(light.getId());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }

    @Override
    public void setLights(List<Light> lightsList) {
        mLightItemAdapter.setItems(lightsList);
    }
}
