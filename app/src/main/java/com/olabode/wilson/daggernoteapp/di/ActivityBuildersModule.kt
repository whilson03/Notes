package com.olabode.wilson.daggernoteapp.di

import com.olabode.wilson.daggernoteapp.MainActivity
import com.olabode.wilson.daggernoteapp.di.main.MainFragmentsBuildersModule
import com.olabode.wilson.daggernoteapp.di.main.MainViewModelsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */
@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [
            MainFragmentsBuildersModule::class,
            MainViewModelsModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}
