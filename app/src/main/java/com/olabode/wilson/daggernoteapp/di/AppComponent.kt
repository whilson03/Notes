package com.olabode.wilson.daggernoteapp.di

import android.app.Application
import com.olabode.wilson.daggernoteapp.BaseApp
import com.olabode.wilson.daggernoteapp.di.repo.RepositoryModule
import com.olabode.wilson.daggernoteapp.di.work.WorkerModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 *   Created by OLABODE WILSON on 2020-03-03 .
 */

@Singleton
@Component(

    modules =
    [
        AndroidSupportInjectionModule::class,
        PersistenceModule::class,
        ActivityBuildersModule::class,
        ViewModelFactoryModule::class,
        RepositoryModule::class,
        WorkerModule::class

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