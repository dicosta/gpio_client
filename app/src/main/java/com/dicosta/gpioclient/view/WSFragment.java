package com.dicosta.gpioclient.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicosta.gpioclient.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WSFragment extends Fragment {

    public WSFragment() {
        // Required empty public constructor
    }

    public static WSFragment newInstance() {
        WSFragment fragment = new WSFragment();
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
        return inflater.inflate(R.layout.fragment_ws, container, false);
    }

}
