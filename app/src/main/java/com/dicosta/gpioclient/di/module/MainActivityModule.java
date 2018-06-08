package com.dicosta.gpioclient.di.module;

import android.support.v7.app.AppCompatActivity;

import com.dicosta.gpioclient.di.scope.PerActivity;
import com.dicosta.gpioclient.di.scope.PerFragment;
import com.dicosta.gpioclient.view.activity.MainActivity;
import com.dicosta.gpioclient.view.activity.BaseActivity;
import com.dicosta.gpioclient.view.fragment.BLEFragment;
import com.dicosta.gpioclient.view.fragment.HTTPFragment;
import com.dicosta.gpioclient.view.fragment.WSFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by diego on 05/02/18.
 */

@Module(includes = BaseActivityModule.class)
public abstract class MainActivityModule {

    /**
     * Provides the injector for the {@link HTTPFragment}, which has access to the dependencies
     * provided by this activity and application instance (singleton scoped objects).
     */
    @PerFragment
    @ContributesAndroidInjector(modules = HTTPFragmentModule.class)
    abstract HTTPFragment httpFragmentInjector();

    /**
     * Provides the injector for the {@link com.dicosta.gpioclient.view.fragment.WSFragment}, which has access to the dependencies
     * provided by this activity and application instance (singleton scoped objects).
     */
    @PerFragment
    @ContributesAndroidInjector(modules = WSFragmentModule.class)
    abstract WSFragment wsFragmentInjector();

    /**
     * Provides the injector for the {@link com.dicosta.gpioclient.view.fragment.WSFragment}, which has access to the dependencies
     * provided by this activity and application instance (singleton scoped objects).
     */
    @PerFragment
    @ContributesAndroidInjector(modules = BLEFragmentModule.class)
    abstract BLEFragment bleFragmentInjector();

    /**
     * As per the contract specified in {@link BaseActivityModule}; "This must be included in all
     * activity modules, which must provide a concrete implementation of {@link AppCompatActivity}."
     * <p>
     * This provides the activity required to inject the
     * {@link BaseActivityModule#ACTIVITY_FRAGMENT_MANAGER} into the
     * {@link BaseActivity}.
     *
     * @param mainActivity the activity
     * @return the activity
     */
    @Binds
    @PerActivity
    abstract AppCompatActivity appCompatActivity(MainActivity mainActivity);
}
