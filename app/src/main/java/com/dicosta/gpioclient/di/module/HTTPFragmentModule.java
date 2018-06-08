package com.dicosta.gpioclient.di.module;

import android.support.v4.app.Fragment;

import com.dicosta.gpioclient.di.scope.PerFragment;
import com.dicosta.gpioclient.view.HTTPView;
import com.dicosta.gpioclient.view.fragment.HTTPFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

/**
 * Created by diego on 05/02/18.
 */

@Module(includes = { BaseFragmentModule.class, HTTPPresenterModule.class})
abstract class HTTPFragmentModule {

    /**
     * As per the contract specified in {@link BaseFragmentModule}; "This must be included in all
     * fragment modules, which must provide a concrete implementation of {@link Fragment}
     * and named {@link BaseFragmentModule#FRAGMENT}.
     *
     * @param httpFragment the main fragment
     * @return the fragment
     */

    @Binds
    @Named(BaseFragmentModule.FRAGMENT)
    @PerFragment
    abstract Fragment fragment(HTTPFragment httpFragment);

    @Binds
    @PerFragment
    abstract HTTPView httpView(HTTPFragment httpFragment);
}
