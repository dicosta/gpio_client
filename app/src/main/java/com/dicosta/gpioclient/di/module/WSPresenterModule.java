package com.dicosta.gpioclient.di.module;

import com.dicosta.gpioclient.di.scope.PerFragment;
import com.dicosta.gpioclient.presenter.WSFragmentPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class WSPresenterModule {
    @Binds
    @PerFragment
    abstract WSFragmentPresenter wsFragmentPresenter(WSFragmentPresenter wsFragmentPresenter);
}
