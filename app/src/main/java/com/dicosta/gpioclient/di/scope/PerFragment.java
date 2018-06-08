package com.dicosta.gpioclient.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by diego on 31/01/18.
 */


@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerFragment {
}