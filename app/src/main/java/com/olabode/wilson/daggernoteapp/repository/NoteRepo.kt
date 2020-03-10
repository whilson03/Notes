package com.olabode.wilson.daggernoteapp.repository

import androidx.lifecycle.LiveData
import com.olabode.wilson.daggernoteapp.models.Note

/**
 *   Created by OLABODE WILSON on 2020-03-03.
 */

interface NoteRepo {
    fun getAllNotes(): LiveData<List<Note>>

    fun getAllFavouriteNotes(): LiveData<List<Note>>

    fun getAllTrashNotes(): LiveData<List<Note>>

    fun getAllNotesCount(): LiveData<Int>

    fun getAllFavouriteNotesCount(): LiveData<Int>

    fun getAllTrashItemsCount(): LiveData<Int>

    suspend fun insertNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun addToFavourite(note: Note)

    suspend fun removeFromFavourite(note: Note)

    suspend fun addToTrash(note: Note)

    suspend fun removeFromTrash(note: Note)

    suspend fun deleteAllFromTrash()
}