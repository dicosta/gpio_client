package com.dicosta.gpioclient.di.module;

import android.support.v4.app.Fragment;

import com.dicosta.gpioclient.di.scope.PerChildFragment;
import com.dicosta.gpioclient.view.BLEDeviceView;
import com.dicosta.gpioclient.view.fragment.BLEDeviceFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

@Module(includes = { BaseChildFragmentModule.class, BLEDevicePresenterModule.class})
abstract class BLEDeviceFragmentModule {
    /**
     * As per the contract specified in {@link BaseFragmentModule}; "This must be included in all
     * fragment modules, which must provide a concrete implementation of {@link Fragment}
     * and named {@link BaseFragmentModule#FRAGMENT}.
     *
     * @param bleDeviceFragment the main fragment
     * @return the fragment
     */
    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @PerChildFragment
    abstract Fragment fragment(BLEDeviceFragment bleDeviceFragment);

    @Binds
    @PerChildFragment
    abstract BLEDeviceView bleDeviceView(BLEDeviceFragment bleDeviceFragment);
}
