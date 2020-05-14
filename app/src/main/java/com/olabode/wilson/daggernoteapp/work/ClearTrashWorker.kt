package com.olabode.wilson.daggernoteapp.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import javax.inject.Inject
import javax.inject.Provider

/**
 *   Created by OLABODE WILSON on 5/5/20.
 */
class ClearTrashWorker @Inject constructor(
    private val appContext: Context,
    private val params: WorkerParameters,
    private val repo: NotesRepository
) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "ClearTrashWorker"
    }


    override suspend fun doWork(): Result {
        repo.deleteAllFromTrash()
        return Result.success()
    }

    class Factory @Inject constructor(
        private val noteRepo: Provider<NotesRepository>
    ) : ChildWorkerFactory {
        override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
            return ClearTrashWorker(
                appContext,
                params,
                noteRepo.get()
            )
        }
    }

}

interface ChildWorkerFactory {
    fun create(appContext: Context, params: WorkerParameters): ListenableWorker
}


class AppWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out ListenableWorker>,
            @JvmSuppressWildcards Provider<ChildWorkerFactory>>
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val foundEntry =
            workerFactories.entries.find { Class.forName(workerClassName).isAssignableFrom(it.key) }
        val factoryProvider = foundEntry?.value
            ?: throw IllegalArgumentException("unknown worker class name: $workerClassName")
        return factoryProvider.get().create(appContext, workerParameters)
    }
}