package com.dicosta.gpioclient.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicosta.gpioclient.R;

public class BLEFragment extends Fragment {
    public BLEFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ble, container, false);
    }
}
