package com.dicosta.gpioclient.di.module;

import android.support.v4.app.Fragment;

import com.dicosta.gpioclient.di.scope.PerChildFragment;
import com.dicosta.gpioclient.view.BLEScanView;
import com.dicosta.gpioclient.view.fragment.BLEScanFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

@Module(includes = { BaseChildFragmentModule.class, BLEScanPresenterModule.class})
abstract class BLEScanFragmentModule {
    /**
     * As per the contract specified in {@link BaseFragmentModule}; "This must be included in all
     * fragment modules, which must provide a concrete implementation of {@link Fragment}
     * and named {@link BaseFragmentModule#FRAGMENT}.
     *
     * @param bleScanFragment the main fragment
     * @return the fragment
     */
    @Binds
    @Named(BaseChildFragmentModule.CHILD_FRAGMENT)
    @PerChildFragment
    abstract Fragment fragment(BLEScanFragment bleScanFragment);

    @Binds
    @PerChildFragment
    abstract BLEScanView bleScanView(BLEScanFragment bleScanFragment);
}
