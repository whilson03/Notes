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
        notesDao.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        notesDao.upDateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        notesDao.deleteNote(note)
    }

}