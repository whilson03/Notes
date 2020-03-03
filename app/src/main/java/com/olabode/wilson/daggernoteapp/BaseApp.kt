package com.olabode.wilson.daggernoteapp

import com.olabode.wilson.daggernoteapp.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */
class BaseApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}