package com.olabode.wilson.daggernoteapp.di.main;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by OLABODE WILSON on 2020-03-03.
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface MainScope {
}
