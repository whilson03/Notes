package com.olabode.wilson.daggernoteapp.di

import androidx.lifecycle.ViewModelProvider
import com.olabode.wilson.daggernoteapp.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModel(viewModelProviderFactory: ViewModelProviderFactory):
        ViewModelProvider.Factory
}
