package com.olabode.wilson.daggernoteapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.olabode.wilson.daggernoteapp.data.NotesDao
import com.olabode.wilson.daggernoteapp.data.Result
import com.olabode.wilson.daggernoteapp.data.Result.Success
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

    override fun getNotesByName(): LiveData<Result<List<Note>>> {
        return notesDao.getNotesByName().map {
            checkEmptyStatus(it)
        }
    }


    override fun getNotesByLastModified(): LiveData<Result<List<Note>>> {
        return notesDao.getNotesByLastModified().map {
            checkEmptyStatus(it)
        }
    }


    override fun getNotesByDateCreated(): LiveData<Result<List<Note>>> {
        return notesDao.getNotesByDateCreated().map {
            checkEmptyStatus(it)
        }
    }


    override fun getFavNotesByName(): LiveData<Result<List<Note>>> {
        return notesDao.getFavNotesByName().map {
            checkEmptyStatus(it)
        }
    }


    override fun getFavNotesByLastModified(): LiveData<Result<List<Note>>> {
        return notesDao.getFavNotesByLastModified().map {
            checkEmptyStatus(it)
        }
    }


    override fun getFavNotesByDateCreated(): LiveData<Result<List<Note>>> {
        return notesDao.getFavNotesByDateCreated().map {
            checkEmptyStatus(it)
        }
    }


    override fun getAllTrashNotes(): LiveData<Result<List<Note>>> {
        return notesDao.getTrashedNotes().map {
            checkEmptyStatus(it)
        }
    }


    override fun getAllTrashedNotesByDateAddedRecent(): LiveData<Result<List<Note>>> {
        return notesDao.getTrashedNotesByDateAddedRecent().map {
            checkEmptyStatus(it)
        }
    }


    override fun getTrashedNotesByDateAddedOlder(): LiveData<Result<List<Note>>> {
        return notesDao.getTrashedNotesByDateAddedOlder().map {
            checkEmptyStatus(it)
        }
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
            note.isFavourite = true
            notesDao.upDateNote(note)
        }
    }


    override suspend fun removeFromFavourite(note: Note) {
        withContext(Dispatchers.IO) {
            note.isFavourite = false
            notesDao.upDateNote(note)
        }
    }


    override suspend fun addToTrash(note: Note) {
        withContext(Dispatchers.IO) {
            note.isTrashItem = true
            notesDao.upDateNote(note)
        }
    }


    override suspend fun removeFromTrash(note: Note) {
        withContext(Dispatchers.IO) {
            note.isTrashItem = false
            notesDao.upDateNote(note)
        }
    }

    override suspend fun deleteAllFromTrash() {
        notesDao.emptyTrash()
    }


    private fun checkEmptyStatus(it: List<Note>): Result<List<Note>> {
        return when (it.isNotEmpty()) {
            true -> Success(it)
            false -> Result.Empty
        }
    }

}