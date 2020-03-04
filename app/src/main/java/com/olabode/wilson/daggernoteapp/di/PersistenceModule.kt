package com.olabode.wilson.daggernoteapp.di

import android.app.Application
import androidx.annotation.NonNull
import androidx.room.Room
import com.olabode.wilson.daggernoteapp.data.NotesDao
import com.olabode.wilson.daggernoteapp.data.NotesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */
@Module
class PersistenceModule {

    @Singleton
    @Provides
    fun provideAppDatabaseInstance(@NonNull application: Application): NotesDatabase {
        return Room.databaseBuilder(
            application,
            NotesDatabase::class.java,
            "notes_database"
        ).fallbackToDestructiveMigration().build()
    }


    @Singleton
    @Provides
    fun provideNotesDao(@NonNull notesDatabase: NotesDatabase): NotesDao {
        return notesDatabase.noteDao()
    }

}