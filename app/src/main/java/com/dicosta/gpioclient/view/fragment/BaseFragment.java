package com.dicosta.gpioclient.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.dicosta.gpioclient.di.module.BaseFragmentModule;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

/**
 * Created by diego on 01/02/18.
 */

public abstract class BaseFragment extends Fragment implements HasSupportFragmentInjector {

    @Nullable
    private Unbinder mViewUnbinder;

    @Inject
    protected Context activityContext;

    // Note that this should not be used within a child fragment.
    @Inject
    @Named(BaseFragmentModule.CHILD_FRAGMENT_MANAGER)
    protected FragmentManager childFragmentManager;

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);


        // No need to check if getView() is null because this lifecycle method will
        // not get called if the view returned in onCreateView, if any, is null.
        mViewUnbinder = ButterKnife.bind(this, getView());
    }

    @Override
    public void onDestroyView() {
        // This lifecycle method still gets called even if onCreateView returns a null view.
        if (mViewUnbinder != null) {
            mViewUnbinder.unbind();
        }

        super.onDestroyView();
    }

    @Override
    public final AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }

    protected final void addChildFragment(@IdRes int containerViewId, Fragment fragment) {
        childFragmentManager.beginTransaction()
                .add(containerViewId, fragment)
                .commit();
    }

    protected final void replaceChildFragment(@IdRes int containerViewId, Fragment fragment) {
        childFragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }
}