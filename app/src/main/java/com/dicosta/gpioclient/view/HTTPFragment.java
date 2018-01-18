package com.dicosta.gpioclient.view;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicosta.gpioclient.R;
import com.dicosta.gpioclient.adapter.LightItemAdapter;
import com.dicosta.gpioclient.domain.Light;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HTTPFragment extends Fragment {

    @BindView(R.id.lights_list)
    RecyclerView mLightsList;

    private Unbinder mUnbinder;
    private LightItemAdapter mLightItemAdapter;

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

        List<Light> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Light newLight = new Light();

            if (i == 1) {
                newLight.setName("Red Light on Pin #1");
                newLight.setState(Light.STATE_ON);
            } else if (i == 2) {
                newLight.setName("Light " + String.valueOf(i));
                newLight.setState(Light.STATE_OFF);
            } else if (i == 3) {
                newLight.setName("Light " + String.valueOf(i));
                newLight.setState(Light.STATE_BLINK);
            } else {
                newLight.setName("Light " + String.valueOf(i));
                newLight.setState(Light.STATE_OFF);
            }

            list.add(newLight);
        }
        mLightItemAdapter.setItems(list);

        mLightsList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mLightsList.setAdapter(mLightItemAdapter);

        mLightItemAdapter.setLightItemAdapterListener(new LightItemAdapter.LightItemAdapterListener() {
            @Override
            public void onSwitchTurnOnClicked(Light light) {
                Log.d("LIGHT ADAPTER LISTENER", "turn on clicked");
            }

            @Override
            public void onSwitchTurnOffClicked(Light light) {
                Log.d("LIGHT ADAPTER LISTENER", "turn off clicked");
            }

            @Override
            public void onStartBlinkClicked(Light light) {
                Log.d("LIGHT ADAPTER LISTENER", "start blink clicked");
            }

            @Override
            public void onStopBlinkClicked(Light light) {
                Log.d("LIGHT ADAPTER LISTENER", "stop blink clicked");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mUnbinder.unbind();
    }
}
