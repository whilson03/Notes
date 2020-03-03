package com.olabode.wilson.daggernoteapp.di

import android.app.Application
import com.olabode.wilson.daggernoteapp.BaseApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 *   Created by OLABODE WILSON on 2020-03-03 .
 */

@Singleton
@Component(
    modules =
    [
        AndroidInjectionModule::class,
        PersistenceModule::class
    ]
)
interface AppComponent : AndroidInjector<BaseApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}