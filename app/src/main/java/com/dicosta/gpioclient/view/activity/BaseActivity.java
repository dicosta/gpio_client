package com.dicosta.gpioclient.view.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.dicosta.gpioclient.di.module.BaseActivityModule;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by diego on 31/01/18.
 */

public abstract class BaseActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    @Named(BaseActivityModule.ACTIVITY_FRAGMENT_MANAGER)
    protected FragmentManager fragmentManager;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public final AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentInjector;
    }

    protected final void addFragment(@IdRes int containerViewId, Fragment fragment) {
        fragmentManager.beginTransaction()
                .add(containerViewId, fragment)
                .commit();
    }

    protected final void replaceFragment(@IdRes int containerViewId, Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }
}
