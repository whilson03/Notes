package com.olabode.wilson.daggernoteapp.di.annotations

import androidx.work.ListenableWorker
import dagger.MapKey
import kotlin.reflect.KClass

/**
 *   Created by OLABODE WILSON on 5/10/20.
 */
@MapKey
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)