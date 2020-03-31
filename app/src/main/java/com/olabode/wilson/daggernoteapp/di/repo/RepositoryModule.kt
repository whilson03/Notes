package com.olabode.wilson.daggernoteapp.di.repo

import com.olabode.wilson.daggernoteapp.data.LabelDao
import com.olabode.wilson.daggernoteapp.data.NotesDao
import com.olabode.wilson.daggernoteapp.repository.NotesRepository
import com.olabode.wilson.daggernoteapp.repository.label.LabelRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *   Created by OLABODE WILSON on 2020-03-04.
 */
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNoteRepository(notesDao: NotesDao) = NotesRepository(notesDao)


    @Singleton
    @Provides
    fun provideLabelRepository(labelDao: LabelDao) = LabelRepository(labelDao)

}