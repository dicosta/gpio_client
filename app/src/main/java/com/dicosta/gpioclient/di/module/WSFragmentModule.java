package com.dicosta.gpioclient.di.module;

import android.support.v4.app.Fragment;

import com.dicosta.gpioclient.di.scope.PerFragment;
import com.dicosta.gpioclient.view.WSView;
import com.dicosta.gpioclient.view.fragment.WSFragment;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

@Module(includes = BaseFragmentModule.class)
public abstract class WSFragmentModule {

    /**
     * As per the contract specified in {@link BaseFragmentModule}; "This must be included in all
     * fragment modules, which must provide a concrete implementation of {@link Fragment}
     * and named {@link BaseFragmentModule#FRAGMENT}.
     *
     * @param wsFragment the main fragment
     * @return the fragment
     */
    @Binds
    @Named(BaseFragmentModule.FRAGMENT)
    @PerFragment
    abstract Fragment fragment(WSFragment wsFragment);

    @Binds
    @PerFragment
    abstract WSView httpView(WSFragment wsFragment);
}
