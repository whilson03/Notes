package com.olabode.wilson.daggernoteapp.repository

import androidx.lifecycle.LiveData
import com.olabode.wilson.daggernoteapp.data.NotesDao
import com.olabode.wilson.daggernoteapp.models.Note
import javax.inject.Inject
import javax.inject.Singleton

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */
@Singleton
class NotesRepository @Inject constructor(
    val notesDao: NotesDao
) : NoteRepo {


    override suspend fun getAllNotes(): LiveData<List<Note>> {
        return notesDao.getAllNotes()
    }


}