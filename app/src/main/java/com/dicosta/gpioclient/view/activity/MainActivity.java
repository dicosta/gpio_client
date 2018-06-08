package com.dicosta.gpioclient.view.activity;

import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;

import com.dicosta.gpioclient.R;
import com.dicosta.gpioclient.view.fragment.BLEFragment;
import com.dicosta.gpioclient.view.fragment.HTTPFragment;
import com.dicosta.gpioclient.view.fragment.WSFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mBottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_http: {
                            replaceFragment(R.id.container_panel, HTTPFragment.newInstance());
                            break;
                        }
                        case R.id.action_ws: {
                            replaceFragment(R.id.container_panel, WSFragment.newInstance());
                            break;
                        }
                        case R.id.action_ble: {
                            replaceFragment(R.id.container_panel, BLEFragment.newInstance());
                            break;
                        }

                    }
                    return true;
                });


        mBottomNavigationView.setSelectedItemId(R.id.action_http);
    }
}
