package com.olabode.wilson.daggernoteapp

import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import androidx.work.*
import com.olabode.wilson.daggernoteapp.di.DaggerAppComponent
import com.olabode.wilson.daggernoteapp.work.AppWorkerFactory
import com.olabode.wilson.daggernoteapp.work.ClearTrashWorker
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */
class BaseApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    private fun delayedInit() = applicationScope.launch {
        setApplicationMode()
        setupRecurringWork()
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


    private suspend fun setApplicationMode() {
        withContext(Dispatchers.Main) {
            if (isDarkMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private suspend fun isDarkMode(): Boolean {
        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return withContext(Dispatchers.IO) {
            preferences.getBoolean(getString(R.string.SHARED_PREF_DARK_MODE_KEY), false)
        }
    }

    private fun configureWorkManager() {
        val config = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
        WorkManager.initialize(this, config)
    }


    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false)
            .setRequiresCharging(false)

            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(false)
                }
            }.build()

        val repeatingRequest =
            PeriodicWorkRequestBuilder<ClearTrashWorker>(1, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build()


        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            ClearTrashWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }

}