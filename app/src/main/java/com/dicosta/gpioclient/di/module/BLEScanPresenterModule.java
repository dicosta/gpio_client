package com.dicosta.gpioclient.di.module;

import com.dicosta.gpioclient.di.scope.PerChildFragment;
import com.dicosta.gpioclient.di.scope.PerFragment;
import com.dicosta.gpioclient.presenter.BLEScanPresenter;
import com.dicosta.gpioclient.presenter.BasePresenter;
import com.dicosta.gpioclient.view.BLEScanView;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class BLEScanPresenterModule {
    @Provides
    @PerChildFragment
    static BasePresenter<BLEScanView> bleScanFragmentPresenter(BLEScanPresenter bleScanPresenter) {
        return bleScanPresenter;
    }
}
