package com.dicosta.gpioclient.di.module;

import android.support.v4.app.Fragment;

import com.dicosta.gpioclient.di.scope.PerChildFragment;
import com.dicosta.gpioclient.di.scope.PerFragment;
import com.dicosta.gpioclient.view.fragment.BLEDeviceFragment;
import com.dicosta.gpioclient.view.fragment.BLEFragment;
import com.dicosta.gpioclient.view.fragment.BLEScanFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module(includes = { BaseFragmentModule.class })
public abstract class BLEFragmentModule {

    /**
     * Provides the injector for the {@link BLEScanFragment}, which has access to the
     * dependencies provided by this fragment and activity and application instance
     * (singleton scoped objects).
     */
    @PerChildFragment
    @ContributesAndroidInjector(modules = BLEScanFragmentModule.class)
    abstract BLEScanFragment BLEScanFragmentInjector();

    /**
     * Provides the injector for the {@link BLEDeviceFragment}, which has access to the
     * dependencies provided by this fragment and activity and application instance
     * (singleton scoped objects).
     */
    @PerChildFragment
    @ContributesAndroidInjector(modules = BLEDeviceFragmentModule.class)
    abstract BLEDeviceFragment BLEDeviceFragmentInjector();


    /**
     * As per the contract specified in {@link BaseFragmentModule}; "This must be included in all
     * fragment modules, which must provide a concrete implementation of {@link Fragment}
     * and named {@link BaseFragmentModule#FRAGMENT}.
     *
     * @param bleFragment the main fragment
     * @return the fragment
     */
    @Binds
    @Named(BaseFragmentModule.FRAGMENT)
    @PerFragment
    abstract Fragment fragment(BLEFragment bleFragment);

}
