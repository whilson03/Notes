package com.olabode.wilson.daggernoteapp.repository

import androidx.lifecycle.LiveData
import com.olabode.wilson.daggernoteapp.data.NotesDao
import com.olabode.wilson.daggernoteapp.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    override fun getAllFavouriteNotes(): LiveData<List<Note>> {
        return notesDao.getFavouriteNotes()
    }

    override fun getAllTrashNotes(): LiveData<List<Note>> {
        return notesDao.getTrashedNotes()
    }

    override fun getAllNotesCount(): LiveData<Int> {
        return notesDao.countNoteTable()
    }

    override fun getAllFavouriteNotesCount(): LiveData<Int> {
        return notesDao.countFavouriteNotes()
    }

    override fun getAllTrashItemsCount(): LiveData<Int> {
        return notesDao.countTrashNotes()
    }

    override suspend fun insertNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.insertNote(note)
        }
    }

    override suspend fun updateNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.upDateNote(note)
        }
    }

    override suspend fun deleteNote(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.deleteNote(note)
        }
    }

    override suspend fun addToFavourite(note: Note) {
        withContext(Dispatchers.IO) {
            note.isFavourite = 1
            notesDao.upDateNote(note)
        }
    }

    override suspend fun removeFromFavourite(note: Note) {
        withContext(Dispatchers.IO) {
            note.isFavourite = 0
            notesDao.upDateNote(note)
        }
    }

    override suspend fun addToTrash(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.upDateNote(note)
        }
    }

    override suspend fun removeFromTrash(note: Note) {
        withContext(Dispatchers.IO) {
            notesDao.upDateNote(note)
        }
    }

    override suspend fun deleteAllFromTrash() {
        notesDao.emptyTrash()
    }

}