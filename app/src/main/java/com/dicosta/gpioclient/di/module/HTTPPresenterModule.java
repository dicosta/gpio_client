package com.dicosta.gpioclient.di.module;

import com.dicosta.gpioclient.api.LightAPI;
import com.dicosta.gpioclient.di.scope.PerFragment;
import com.dicosta.gpioclient.net.HttpServiceFactory;
import com.dicosta.gpioclient.presenter.BasePresenter;
import com.dicosta.gpioclient.presenter.HTTPFragmentPresenter;
import com.dicosta.gpioclient.view.HTTPView;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


@Module
public abstract class HTTPPresenterModule {

    @Provides
    @PerFragment
    static BasePresenter<HTTPView> httpFragmentPresenter(HTTPFragmentPresenter httpFragmentPresenter) {
        return httpFragmentPresenter;
    }

    @Provides
    @PerFragment
    static LightAPI providesLightAPI(HttpServiceFactory httpServiceFactory) {
        return httpServiceFactory.create(LightAPI.class);
    }
}
