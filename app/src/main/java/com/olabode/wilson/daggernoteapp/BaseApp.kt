package com.olabode.wilson.daggernoteapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
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

}