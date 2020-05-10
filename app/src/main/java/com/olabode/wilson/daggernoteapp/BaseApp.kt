package com.olabode.wilson.daggernoteapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.work.Configuration
import androidx.work.WorkManager
import com.olabode.wilson.daggernoteapp.di.DaggerAppComponent
import com.olabode.wilson.daggernoteapp.work.AppWorkerFactory
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */
class BaseApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }


    @Inject
    lateinit var workerFactory: AppWorkerFactory


    override fun onCreate() {
        super.onCreate()
        if (isDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun isDarkMode(): Boolean {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return preferences.getBoolean(getString(R.string.SHARED_PREF_DARK_MODE_KEY), false)
    }

    private fun configureWorkManager() {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(this, config)
    }

}