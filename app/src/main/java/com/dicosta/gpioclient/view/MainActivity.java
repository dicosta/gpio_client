package com.dicosta.gpioclient.view;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.dicosta.gpioclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_http: {
                                loadFragment(HTTPFragment.newInstance());
                                break;
                            }
                            case R.id.action_ws: {
                                loadFragment(WSFragment.newInstance());
                                break;
                            }
                            case R.id.action_ble: {
                                loadFragment(BLEFragment.newInstance());
                                break;
                            }

                        }
                        return true;
                    }
                });


        mBottomNavigationView.setSelectedItemId(R.id.action_http);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_panel, fragment);
        fragmentTransaction.commit();
    }
}
