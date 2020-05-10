package com.olabode.wilson.daggernoteapp.di.work

import com.olabode.wilson.daggernoteapp.di.annotations.WorkerKey
import com.olabode.wilson.daggernoteapp.work.ChildWorkerFactory
import com.olabode.wilson.daggernoteapp.work.ClearTrashWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 *   Created by OLABODE WILSON on 5/10/20.
 */
@Module
abstract class WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(ClearTrashWorker::class)
    abstract fun bindClearTrashWorker(factory: ClearTrashWorker.Factory): ChildWorkerFactory
}

