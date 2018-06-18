package com.dicosta.gpioclient.di.module;

import com.dicosta.gpioclient.di.scope.PerChildFragment;
import com.dicosta.gpioclient.presenter.BLEDevicePresenter;
import com.dicosta.gpioclient.presenter.BasePresenter;
import com.dicosta.gpioclient.view.BLEDeviceView;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class BLEDevicePresenterModule {
    @Provides
    @PerChildFragment
    static BasePresenter<BLEDeviceView> bleDevicePresenter(BLEDevicePresenter bleDevicePresenter) {
        return bleDevicePresenter;
    }
}
