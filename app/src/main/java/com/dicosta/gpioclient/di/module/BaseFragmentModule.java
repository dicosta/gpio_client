package com.dicosta.gpioclient.di.module;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.dicosta.gpioclient.di.scope.PerFragment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by diego on 01/02/18.
 */

@Module
public abstract class BaseFragmentModule {

    public static final String FRAGMENT = "BaseFragmentModule.fragment";

    public static final String CHILD_FRAGMENT_MANAGER = "BaseFragmentModule.childFragmentManager";

    @Provides
    @Named(CHILD_FRAGMENT_MANAGER)
    @PerFragment
    static FragmentManager childFragmentManager(@Named(FRAGMENT) Fragment fragment) {
        return fragment.getChildFragmentManager();
    }
}
