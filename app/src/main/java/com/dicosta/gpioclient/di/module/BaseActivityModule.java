package com.dicosta.gpioclient.di.module;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.dicosta.gpioclient.di.scope.PerActivity;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Created by diego on 01/02/18.
 */

@Module
public abstract class BaseActivityModule {

    public static final String ACTIVITY_FRAGMENT_MANAGER = "BaseActivityModule.activityFragmentManager";

    @Binds
    @PerActivity
    abstract Context activityContext(AppCompatActivity activity);

    @Provides
    @Named(ACTIVITY_FRAGMENT_MANAGER)
    @PerActivity
    static FragmentManager activityFragmentManager(AppCompatActivity activity) {
        return activity.getSupportFragmentManager();
    }
}