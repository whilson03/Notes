package com.olabode.wilson.daggernoteapp.repository

import android.util.Log
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
    private val notesDao: NotesDao
) : NoteRepo {


    override fun getAllNotes(): LiveData<List<Note>> {
        return notesDao.getAllNotes()
    }

    suspend fun insert(note: Note) {
        Log.i("Note ", note.toString())
        notesDao.insertNote(note)

    }


}