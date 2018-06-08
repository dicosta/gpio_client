package com.dicosta.gpioclient.di.component;

import com.dicosta.gpioclient.GPIOApplication;
import com.dicosta.gpioclient.di.module.GPIOModule;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = GPIOModule.class)
public interface GPIOComponent extends AndroidInjector<GPIOApplication> {
    void inject(GPIOApplication app);

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<GPIOApplication> {
    }
}
